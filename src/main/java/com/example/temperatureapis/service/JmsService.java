package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.Temperature;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsOperations;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;


public interface JmsService {
    String TEMPERATURE_QUEUE = "temperatureQueue";

    JmsOperations getJms();

    default void addToQueue(List<Temperature> temperatureList) {
        if (temperatureList == null || temperatureList.isEmpty())
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "temperature list cannot be null or empty");

        temperatureList.parallelStream().forEach(t -> getJms().convertAndSend(JmsService.TEMPERATURE_QUEUE, t));
    }
}
