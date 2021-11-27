package com.example.temperatureapis.service;

import com.example.temperatureapis.domain.Temperature;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JmsServiceActiveMQ implements JmsService {
    private final TemperatureService temperatureService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = TEMPERATURE_QUEUE)
    protected void receiveTemperature(Temperature temperature) {
        temperatureService.save(temperature);
    }

    @Override
    public JmsOperations getJms() {
        return jmsTemplate;
    }
}
