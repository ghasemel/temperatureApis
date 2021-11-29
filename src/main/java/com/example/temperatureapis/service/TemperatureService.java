package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;

import java.util.List;

public interface TemperatureService {
    void save(Temperature temperature);
    void save(List<Temperature> temperature);
    List<AggregatedData> retrieveHourlyTemperature(long fromEpoch, long tillEpoch);
    List<AggregatedData> retrieveDailyTemperature(long fromEpoch, long tillEpoch);
}
