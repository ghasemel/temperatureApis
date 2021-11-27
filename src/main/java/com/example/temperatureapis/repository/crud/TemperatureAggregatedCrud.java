package com.example.temperatureapis.repository.crud;

import com.example.temperatureapis.domain.AggregatedData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureAggregatedCrud extends CrudRepository<AggregatedData, Long> {
    List<AggregatedData> findAllByTypeAndIdBetween(char type, long from, long till);
}
