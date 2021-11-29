package com.example.temperatureapis.repository;

import com.example.temperatureapis.constants.Errors;
import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.repository.crud.TemperatureAggregatedCrud;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;


@Component
@Profile("h2Repo")
@RequiredArgsConstructor
@Slf4j
public class H2Repository implements Repository {
    private final TemperatureAggregatedCrud crud;
    protected static final char DAILY = 'D';
    protected static final char HOURLY = 'H';

    @Override
    public void insert(Temperature temperature) {
        log.debug("insert() - temperature: {}", temperature);
        if (temperature == null) {
            log.error("insert() - {}", Errors.NULL_TEMPERATURE);
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, Errors.NULL_TEMPERATURE);
        }

        updateAggregatedData(temperature, ONE_HOUR_IN_SEC, HOURLY);
        updateAggregatedData(temperature, ONE_DAY_IN_SEC, DAILY);
    }

    private void updateAggregatedData(Temperature data, int keyDivider, char type) {
        log.debug("updateAggregatedData() - temperature:{},keyDivider:{},type:{}", data, keyDivider, type);
        final var id = data.getEpoch() / keyDivider;
        final var epoch = id * keyDivider;

        final var aggregatedData = crud.findById(id);
        if (aggregatedData.isEmpty()) {
            var newRecord = new AggregatedData(id, epoch, data.getValue(), type);
            crud.save(newRecord);
        } else {
            aggregatedData.get().updateMaxMin(data.getValue());
            crud.save(aggregatedData.get());
        }
    }

    @Override
    public List<AggregatedData> findHourlyAggregatedData(long fromEpoch, long tillEpoch) {
        log.debug("findHourlyAggregatedData() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return crud.findAllByTypeAndIdBetween(HOURLY, fromEpoch / ONE_HOUR_IN_SEC, tillEpoch / ONE_HOUR_IN_SEC);
    }

    @Override
    public List<AggregatedData> findDailyAggregatedData(long fromEpoch, long tillEpoch) {
        log.debug("findDailyAggregatedData() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return crud.findAllByTypeAndIdBetween(DAILY, fromEpoch / ONE_DAY_IN_SEC, tillEpoch / ONE_DAY_IN_SEC);
    }
}
