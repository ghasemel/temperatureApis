package com.example.temperatureapis.repository;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.exceptionhandler.Errors;
import com.example.temperatureapis.repository.crud.TemperatureAggregatedCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;


@Component
@Profile("h2Repo")
@RequiredArgsConstructor
public class H2Repository implements Repository {
    private final TemperatureAggregatedCrud crud;
    protected static final char DAILY = 'D';
    protected static final char HOURLY = 'H';

    @Override
    public void insert(Temperature temperature) {
        if (temperature == null)
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, Errors.NULL_TEMPERATURE);

        updateAggregatedData(temperature, ONE_HOUR_IN_SEC, HOURLY);
        updateAggregatedData(temperature, ONE_DAY_IN_SEC, DAILY);
    }

    private void updateAggregatedData(Temperature data, int keyDivider, char type) {
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
        return crud.findAllByTypeAndIdBetween(HOURLY, fromEpoch / ONE_HOUR_IN_SEC, tillEpoch / ONE_HOUR_IN_SEC);
    }

    @Override
    public List<AggregatedData> findDailyAggregatedData(long fromEpoch, long tillEpoch) {
        return crud.findAllByTypeAndIdBetween(DAILY, fromEpoch / ONE_DAY_IN_SEC, tillEpoch / ONE_DAY_IN_SEC);
    }
}
