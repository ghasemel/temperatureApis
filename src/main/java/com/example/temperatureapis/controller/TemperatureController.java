package com.example.temperatureapis.controller;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.service.JmsService;
import com.example.temperatureapis.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temperature")
@RequiredArgsConstructor
public class TemperatureController {
    private final JmsService jmsService;
    private final TemperatureService temperatureService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> saveTemperature(@RequestBody List<Temperature> temperatureList) {
        jmsService.addToQueue(temperatureList);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/hourly/{fromEpoch}/{tillEpoch}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AggregatedData>> getHourlyAggregatedData(@PathVariable Long fromEpoch, @PathVariable Long tillEpoch) {
        var report = temperatureService.retrieveHourlyTemperature(fromEpoch, tillEpoch);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/daily/{fromEpoch}/{tillEpoch}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AggregatedData>> getDailyAggregatedData(@PathVariable Long fromEpoch, @PathVariable Long tillEpoch) {
        var report = temperatureService.retrieveDailyTemperature(fromEpoch, tillEpoch);
        return ResponseEntity.ok(report);
    }
}