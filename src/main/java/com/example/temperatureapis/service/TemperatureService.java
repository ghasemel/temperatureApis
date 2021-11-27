package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TemperatureService {
    UUID save(Temperature temperature);
    List<AggregatedData> retrieveHourlyTemperature(long fromEpoch, long tillEpoch);
    List<AggregatedData> retrieveDailyTemperature(long fromEpoch, long tillEpoch);
}
