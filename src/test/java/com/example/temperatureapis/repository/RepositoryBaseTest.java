package com.example.temperatureapis.repository;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class RepositoryBaseTest {

    protected void assertResult(long time, float tempMin, float tempMax, float expectedAggregatedValue,
                              List<AggregatedData> aggregatedResult, int epochBase) {
        assertNotNull(aggregatedResult);
        assertEquals(1, aggregatedResult.size());
        assertSingleResult(time, tempMin, tempMax, expectedAggregatedValue, aggregatedResult.get(0), epochBase);
    }

    protected void assertSingleResult(long time, float tempMin, float tempMax, float expectedAggregatedValue,
                                    AggregatedData aggregatedResult, int epochBase) {
        assertEquals(tempMax, aggregatedResult.getMax());
        assertEquals(tempMin, aggregatedResult.getMin());
        assertEquals(expectedAggregatedValue, aggregatedResult.getAggregatedValue());

        // divide by ONE_HOUR_IN_SEC and multiply by the same value to mask minute and second as HH0000
        assertEquals(time / epochBase * epochBase, aggregatedResult.getEpoch());
    }

    protected void addTemperature(Repository repository, long EPOCH_1, float TEMP_1) {
        repository.insert(Temperature.builder().value(TEMP_1).epoch(EPOCH_1).build());
    }
}
