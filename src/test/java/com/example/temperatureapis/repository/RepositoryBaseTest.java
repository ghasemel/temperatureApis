package com.example.temperatureapis.repository;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class RepositoryBaseTest {

    final long EPOCH_1 = 1638056441L; // 2021-11-27 23:40:41
    final float TEMP_1 = 12.43f;

    final long EPOCH_2 = 1638054000L; // 2021-11-27 23:00:00
    final float TEMP_2 = 43f;

    final long EPOCH_3 = 1637971200L; // 2021-11-27 00:00:00
    final float TEMP_3 = -7.652f;

    final long EPOCH_4 = 1638124068L; // 2021-11-27 00:00:00
    final float TEMP_4 = -17.652f;

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
