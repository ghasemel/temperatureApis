package com.example.temperatureapis.controller;

import com.example.temperatureapis.domain.AggregatedData;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.temperatureapis.constants.Vars.*;

@RestController
@RequestMapping(TEMPERATURE_CONTROLLER)
@RequiredArgsConstructor
@Slf4j
public class TemperatureController {

    private final TemperatureService temperatureService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> saveTemperature(@RequestBody List<Temperature> temperatureList) {
        log.debug("temperatures list to store: {}", temperatureList);
        temperatureService.save(temperatureList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = TEMPERATURE_HOURLY_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AggregatedData>> getHourlyAggregatedData(@PathVariable Long fromEpoch, @PathVariable Long tillEpoch) {
        log.debug("getHourlyAggregatedData - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        var report = temperatureService.retrieveHourlyTemperature(fromEpoch, tillEpoch);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = TEMPERATURE_DAILY_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AggregatedData>> getDailyAggregatedData(@PathVariable Long fromEpoch, @PathVariable Long tillEpoch) {
        log.debug("getDailyAggregatedData - fromEpoch:{},tillEpoch:{}", fromEpoch, tillEpoch);
        var report = temperatureService.retrieveDailyTemperature(fromEpoch, tillEpoch);
        return ResponseEntity.ok(report);
    }
}
