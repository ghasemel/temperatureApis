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

    public AggregatedData(long id, long epoch, float data, char type) {
        this.id = id;
        this.min = data;
        this.max = data;
        this.epoch = epoch;
        this.type = type;
        updateAggregatedValue();
    }

    public AggregatedData(long epoch, float data) {
        this.min = data;
        this.max = data;
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

        if (epoch != that.epoch) return false;
        if (type != that.type) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        int result = (int) (epoch ^ (epoch >>> 32));
        result = 31 * result + (int) type;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
