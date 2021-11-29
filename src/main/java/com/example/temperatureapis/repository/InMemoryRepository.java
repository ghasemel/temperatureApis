package com.example.temperatureapis.repository;

import com.example.temperatureapis.constants.Errors;
import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Profile("inMemoryRepo")
@Slf4j
public class InMemoryRepository implements Repository {
    private final Map<Long, AggregatedData> last24hAggregation = new ConcurrentHashMap<>();
    private final Map<Long, AggregatedData> last7dAggregation = new ConcurrentHashMap<>();

    @Override
    public void insert(Temperature temperature) {
        log.debug("insert() - temperature: {}", temperature);
        if (temperature == null) {
            log.error("insert() - {}", Errors.NULL_TEMPERATURE);
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, Errors.NULL_TEMPERATURE);
        }

        updateAggregatedData(temperature, last24hAggregation, ONE_HOUR_IN_SEC);
        updateAggregatedData(temperature, last7dAggregation, ONE_DAY_IN_SEC);
    }

    @Override
    public List<AggregatedData> findHourlyAggregatedData(long fromEpoch, long tillEpoch) {
        log.debug("findHourlyAggregatedData() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return filterAggregatedData(fromEpoch, tillEpoch, last24hAggregation,  ONE_HOUR_IN_SEC);
    }

    @Override
    public List<AggregatedData> findDailyAggregatedData(long fromEpoch, long tillEpoch) {
        log.debug("findDailyAggregatedData() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return filterAggregatedData(fromEpoch, tillEpoch, last7dAggregation, ONE_DAY_IN_SEC);
    }

    private void updateAggregatedData(Temperature data, Map<Long, AggregatedData> aggregationHistory, int keyDivider) {
        log.debug("updateAggregatedData() - temperature:{},aggregationHistory:{},type:{}",
                data, aggregationHistory, keyDivider);

        final var key = data.getEpoch() / keyDivider;
        final var keyEpoch = key * keyDivider;

        var aggregatedValue = aggregationHistory.get(key);
        if (aggregatedValue == null) {
            aggregationHistory.put(key, new AggregatedData(keyEpoch, data.getValue()));
        } else {
            aggregationHistory.put(key, aggregatedValue.updateMaxMin(data.getValue()));
        }
    }

    private List<AggregatedData> filterAggregatedData(long fromEpoch, long tillEpoch, Map<Long, AggregatedData> aggregationHistory, int epochDivider) {
        log.debug("filterAggregatedData() - fromEpoch:{},tillEpoch:{},aggregationHistory:{},epochDivider:{}",
                fromEpoch, tillEpoch, aggregationHistory, epochDivider);

        var from = fromEpoch / epochDivider;
        var till = tillEpoch / epochDivider;
        return aggregationHistory.entrySet().stream()
                .filter(entry -> entry.getKey() >= from && entry.getKey() <= till)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
