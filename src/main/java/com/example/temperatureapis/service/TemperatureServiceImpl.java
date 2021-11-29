package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.repository.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureServiceImpl implements TemperatureService {

    private final Repository repository;

    @Override
    public void save(Temperature temperature) {
        log.debug("save(): {}", temperature);
        repository.insert(temperature);
    }

    @Override
    public void save(List<Temperature> temperatureList) {
        log.debug("save() - list: {}", temperatureList);

        if (temperatureList == null || temperatureList.isEmpty()) {
            log.error("save() - list cannot be null or empty");
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "temperature list cannot be null or empty");
        }

        temperatureList.forEach(this::save);
    }

    @Override
    public List<AggregatedData> retrieveHourlyTemperature(long fromEpoch, long tillEpoch) {
        log.debug("retrieveHourlyTemperature() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return repository.findHourlyAggregatedData(fromEpoch, tillEpoch);
    }

    @Override
    public List<AggregatedData> retrieveDailyTemperature(long fromEpoch, long tillEpoch) {
        log.debug("retrieveDailyTemperature() - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        return repository.findDailyAggregatedData(fromEpoch, tillEpoch);
    }


}
