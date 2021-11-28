package com.example.temperatureapis.controller;

import com.example.temperatureapis.Constants;
import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.exceptionhandler.Errors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;

import static com.example.temperatureapis.Constants.*;
import static com.example.temperatureapis.TestConstants.*;
import static com.example.temperatureapis.repository.Repository.ONE_HOUR_IN_SEC;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {Constants.H2_PROFILE, Constants.DEV})
class TemperatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Order(1)
    void givenTemperatureDataAsEmptyList_whenSave_thenThrowBadRequestException() throws Exception {
        // given
        var tempList = new ArrayList<Temperature>();
        final var temperature = mapper.writeValueAsString(tempList);

        // when
        mockMvc.perform(
                        post(TEMPERATURE_CONTROLLER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(temperature))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("400 temperature list cannot be null or empty"));
    }

    @Test
    @Order(2)
    void givenFourTemperatureData_whenSave_thenSavedSuccessfully() throws Exception {
        // given
        var tempList = new ArrayList<Temperature>();
        tempList.add(Temperature.builder().epoch(EPOCH_1).value(TEMP_1).build());
        tempList.add(Temperature.builder().epoch(EPOCH_2).value(TEMP_2).build());
        tempList.add(Temperature.builder().epoch(EPOCH_3).value(TEMP_3).build());
        tempList.add(Temperature.builder().epoch(EPOCH_4).value(TEMP_4).build());
        final var temperature = mapper.writeValueAsString(tempList);

        // when
        mockMvc.perform(post(TEMPERATURE_CONTROLLER)
                        .contentType(MediaType.APPLICATION_JSON).content(temperature)
                )
                // then
                .andExpect(status().isOk());

  /*      // when
        addTemperature(repository, EPOCH_1, TEMP_1);

        //then
        final var aggregatedData = repository.findHourlyAggregatedData(EPOCH_1, EPOCH_1);
        assertResult(EPOCH_1, TEMP_1, TEMP_1, TEMP_1, aggregatedData, ONE_HOUR_IN_SEC);*/
    }

    @Test
    @Order(3)
    void givenFourTemperatureData_whenGetHourly_thenAggregatedProperly() throws Exception {
        final var EXPECTED_RESULT = "[{\"epoch\":1638054000,\"min\":12.43,\"max\":43.0,\"aggregatedValue\":27.715},{\"epoch\":1638122400,\"min\":-17.652,\"max\":-17.652,\"aggregatedValue\":-17.652}]";

        // when
        mockMvc.perform(get(TEMPERATURE_CONTROLLER + TEMPERATURE_HOURLY_ENDPOINT, EPOCH_2, EPOCH_4)
                        .accept(MediaType.APPLICATION_JSON)
                )
                // then
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().string(EXPECTED_RESULT));
    }

    @Test
    @Order(4)
    void givenFourTemperatureData_whenGetDaily_thenAggregatedProperly() throws Exception {
        final var EXPECTED_RESULT = "[{\"epoch\":1637971200,\"min\":-7.652,\"max\":43.0,\"aggregatedValue\":17.674},{\"epoch\":1638057600,\"min\":-17.652,\"max\":-17.652,\"aggregatedValue\":-17.652}]";

        // when
        mockMvc.perform(get(TEMPERATURE_CONTROLLER + TEMPERATURE_DAILY_ENDPOINT, EPOCH_2, EPOCH_4)
                        .accept(MediaType.APPLICATION_JSON)
                )
                // then
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().string(EXPECTED_RESULT));
    }
}