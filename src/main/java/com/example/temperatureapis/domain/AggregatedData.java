package com.example.temperatureapis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TEMPERATURE_AGGREGATED_DATA", indexes = @Index(name = "typeAndIdIndex", columnList = "id,type"))
public class AggregatedData {

    @Column(unique = true, nullable = false)
    private long epoch;
    private float min;
    private float max;
    private float aggregatedValue;

    @Column(length = 1)
    @JsonIgnore
    private char type; // 'd' stands for daily  // 'h' stands for hourly

    @Id
    @JsonIgnore
    private long id;

    public AggregatedData(long id, long epoch, float singleData, char type) {
        this.id = id;
        this.min = singleData;
        this.max = singleData;
        this.epoch = epoch;
        this.type = type;
        updateAggregatedValue();
    }

    public AggregatedData(long epoch, float singleData) {
        this.min = singleData;
        this.max = singleData;
        this.epoch = epoch;
        updateAggregatedValue();
    }

    public void updateAggregatedValue() {
        aggregatedValue = (max + min) / 2;
    }

    public AggregatedData updateMaxMin(float data) {
        if (data > max)
            max = data;
        else if (data < min)
            min = data;
        updateAggregatedValue();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregatedData that = (AggregatedData) o;

        if (Float.compare(that.min, min) != 0) return false;
        return Float.compare(that.max, max) == 0;
    }

    @Override
    public int hashCode() {
        int result = (min != +0.0f ? Float.floatToIntBits(min) : 0);
        result = 31 * result + (max != +0.0f ? Float.floatToIntBits(max) : 0);
        return result;
    }
}
