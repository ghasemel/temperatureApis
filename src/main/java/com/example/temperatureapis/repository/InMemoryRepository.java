package com.example.temperatureapis.repository;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Profile("inMemoryRepo")
public class InMemoryRepository implements Repository {
    private final Map<Long, AggregatedData> last24hAggregation = new ConcurrentHashMap<>();
    private final Map<Long, AggregatedData> last7dAggregation = new ConcurrentHashMap<>();

    @Override
    public void insert(Temperature temperature) {
        temperature.setId(UUID.randomUUID());
        updateAggregatedData(temperature, last24hAggregation, ONE_HOUR_IN_SEC);
        updateAggregatedData(temperature, last7dAggregation, ONE_DAY_IN_SEC);
    }

    @Override
    public List<AggregatedData> findHourlyAggregatedData(long fromEpoch, long tillEpoch) {
        return filterAggregatedData(fromEpoch, tillEpoch, last24hAggregation,  ONE_HOUR_IN_SEC);
    }

    @Override
    public List<AggregatedData> findDailyAggregatedData(long fromEpoch, long tillEpoch) {
        return filterAggregatedData(fromEpoch, tillEpoch, last7dAggregation, ONE_DAY_IN_SEC);
    }

    private void updateAggregatedData(Temperature data, Map<Long, AggregatedData> aggregationHistory, int keyDivider) {
        final var key = data.getEpoch() / keyDivider;
        final var keyEpoch = key * keyDivider;

        var aggregatedValue = aggregationHistory.get(key);
        if (aggregatedValue == null) {
            aggregationHistory.put(key, new AggregatedData(keyEpoch, data.getValue()));
        } else {
            aggregationHistory.put(key, aggregatedValue.updateMaxMin(data.getValue()));
        }
    }

    private List<AggregatedData> filterAggregatedData(long fromEpoch, long tillEpoch, Map<Long, AggregatedData> last7dAggregation, int epochDivider) {
        var from = fromEpoch / epochDivider;
        var till = tillEpoch / epochDivider;
        return last7dAggregation.entrySet().stream()
                .filter(entry -> entry.getKey() >= from && entry.getKey() <= till)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
