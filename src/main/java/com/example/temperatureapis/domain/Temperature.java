package com.example.temperatureapis.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Temperature implements Serializable {
    private static final long serialVersionUID = 3172426094028871356L;

    private long epoch;
    private float value;
}
