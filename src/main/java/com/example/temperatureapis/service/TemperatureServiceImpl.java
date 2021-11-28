package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final Repository repository;

    @Override
    public void save(Temperature temperature) {
        repository.insert(temperature);
    }

    @Override
    public List<AggregatedData> retrieveHourlyTemperature(long fromEpoch, long tillEpoch) {
        return repository.findHourlyAggregatedData(fromEpoch, tillEpoch);
    }

    @Override
    public List<AggregatedData> retrieveDailyTemperature(long fromEpoch, long tillEpoch) {
        return repository.findDailyAggregatedData(fromEpoch, tillEpoch);
    }


}
