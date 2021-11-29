package com.example.temperatureapis.domain;

import org.junit.jupiter.api.Test;

import static com.example.temperatureapis.TestConstants.EPOCH_1;
import static com.example.temperatureapis.repository.Repository.ONE_HOUR_IN_SEC;
import static org.junit.jupiter.api.Assertions.*;

class AggregatedDataTest {

    private AggregatedData aggregatedData;

    @Test
    void giveMinMaxAndDataGreaterThanMax_whenUpdateMaxMin_thenMaxUpdated() {
        // given
        final float min = 12.43f;
        final float max = 35.76f;
        final float data = 64.76f;
        aggregatedData = AggregatedData.builder().min(min).max(max).build();

        // when
        aggregatedData.updateMaxMin(data);

        // then
        assertEquals(data, aggregatedData.getMax());
        assertEquals(min, aggregatedData.getMin());
    }

    @Test
    void giveMinMaxAndDataLessThanMax_whenUpdateMaxMin_thenMaxUnchanged() {
        // given
        final float min = 12.43f;
        final float max = 35.76f;
        final float data = 23.98f;
        aggregatedData = AggregatedData.builder().min(min).max(max).build();

        // when
        aggregatedData.updateMaxMin(data);

        // then
        assertEquals(max, aggregatedData.getMax());
        assertEquals(min, aggregatedData.getMin());
    }

    @Test
    void giveMinMaxAndDataLessThanMin_whenUpdateMaxMin_thenMinUpdated() {
        // given
        final float min = 12.43f;
        final float max = 35.76f;
        final float data = 8.98f;
        aggregatedData = AggregatedData.builder().min(min).max(max).build();

        // when
        aggregatedData.updateMaxMin(data);

        // then
        assertEquals(max, aggregatedData.getMax());
        assertEquals(data, aggregatedData.getMin());
    }

    @Test
    void giveMinMaxAndDataBetweenMinAndMax_whenUpdateMaxMin_thenMinUnchanged() {
        // given
        final float min = 12.43f;
        final float max = 35.76f;
        final float data = 20.98f;
        aggregatedData = AggregatedData.builder().min(min).max(max).build();

        // when
        aggregatedData.updateMaxMin(data);

        // then
        assertEquals(max, aggregatedData.getMax());
        assertEquals(min, aggregatedData.getMin());
    }

    @Test
    void giveEpochAndData_whenAggregatedData_thenAssignedProperly() {
        // given
        final long epoch = EPOCH_1;
        final float data = 20.98f;

        // when
        aggregatedData = new AggregatedData(epoch, data);

        // then
        assertEquals(data, aggregatedData.getMax());
        assertEquals(data, aggregatedData.getMin());
        assertEquals(data, aggregatedData.getAggregatedValue());
        assertEquals(epoch, aggregatedData.getEpoch());
    }

    @Test
    void giveEpochAndDataAndId_whenAggregatedData_thenAssignedProperly() {
        // given
        final long id = EPOCH_1 / ONE_HOUR_IN_SEC;
        final long epoch = EPOCH_1;
        final float data = 20.98f;
        final char type = 'H';

        // when
        aggregatedData = new AggregatedData(id, epoch, data, type);

        // then
        assertEquals(data, aggregatedData.getMax());
        assertEquals(data, aggregatedData.getMin());
        assertEquals(data, aggregatedData.getAggregatedValue());
        assertEquals(epoch, aggregatedData.getEpoch());
        assertEquals(id, aggregatedData.getId());
        assertEquals(type, aggregatedData.getType());
    }
}