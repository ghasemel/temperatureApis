package com.example.temperatureapis.repository;


import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;

import java.util.List;

public interface Repository {
    int ONE_HOUR_IN_SEC = 3600;
    int ONE_DAY_IN_SEC = 86400;

    void insert(Temperature temperature);
    List<AggregatedData> findHourlyAggregatedData(long fromEpoch, long tillEpoch);
    List<AggregatedData> findDailyAggregatedData(long fromEpoch, long tillEpoch);
}
