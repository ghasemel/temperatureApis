package com.example.temperatureapis.repository;

import com.example.temperatureapis.domain.Temperature;
import com.example.temperatureapis.exceptionhandler.Errors;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;

import static com.example.temperatureapis.repository.Repository.ONE_DAY_IN_SEC;
import static com.example.temperatureapis.repository.Repository.ONE_HOUR_IN_SEC;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
@ActiveProfiles({ "inMemoryRepo", "dev" })
class InMemoryRepositoryTest extends RepositoryBaseTest {

    private final InMemoryRepository repository = new InMemoryRepository();


    @Test
    void givenTemperatureDataEqualNull_whenInsert_thenThrowBadRequestException() {
        // given
        final Temperature temperature = null;

        var exception = assertThrows(HttpServerErrorException.class, () -> {
            // when
            repository.insert(temperature);
        });

        //then
        assertNotNull(exception);
        assertEquals(new HttpServerErrorException(HttpStatus.BAD_REQUEST, Errors.NULL_TEMPERATURE).getMessage(), exception.getMessage());
    }

    @Test
    void givenOneTemperatureData_whenInsert_thenSavedSuccessfully() {
        // when
        addTemperature(repository, EPOCH_1, TEMP_1);

        //then
        final var aggregatedData = repository.findHourlyAggregatedData(EPOCH_1, EPOCH_1);
        assertResult(EPOCH_1, TEMP_1, TEMP_1, TEMP_1, aggregatedData, ONE_HOUR_IN_SEC);
    }

    @Test
    void givenTwoTemperatureDataOnSameDayAndHour_whenFindAggregatedData_thenAggregatedSuccessfully() {
        // given
        addTemperature(repository, EPOCH_1, TEMP_1);
        addTemperature(repository, EPOCH_2, TEMP_2);

        // when
        final var hourlyAggregated = repository.findHourlyAggregatedData(EPOCH_1, EPOCH_1);
        final var dailyAggregated = repository.findDailyAggregatedData(EPOCH_1, EPOCH_1);

        //then
        assertResult(EPOCH_2, TEMP_1, TEMP_2, (TEMP_1 + TEMP_2) / 2, hourlyAggregated, ONE_HOUR_IN_SEC);
        assertResult(EPOCH_2, TEMP_1, TEMP_2, (TEMP_1 + TEMP_2) / 2, dailyAggregated, ONE_DAY_IN_SEC);
    }

    @Test
    void givenThreeTemperatureDataOnSameDayAndDifferentHours_whenFindAggregatedData_thenAggregatedSuccessfully() {
        // given
        addTemperature(repository, EPOCH_1, TEMP_1);
        addTemperature(repository, EPOCH_2, TEMP_2);
        addTemperature(repository, EPOCH_3, TEMP_3);

        // when
        final var hourlyAggregated = repository.findHourlyAggregatedData(EPOCH_1, EPOCH_1);
        final var dailyAggregated = repository.findDailyAggregatedData(EPOCH_2, EPOCH_2);

        //then
        assertResult(EPOCH_2, TEMP_1, TEMP_2, (TEMP_1 + TEMP_2) / 2, hourlyAggregated, ONE_HOUR_IN_SEC);
        assertResult(EPOCH_2, TEMP_3, TEMP_2, (TEMP_2 + TEMP_3) / 2, dailyAggregated, ONE_DAY_IN_SEC);
    }

    @Test
    void givenFourTemperatureDataOnDifferentDayAndDifferentHours_whenFindAggregatedData_thenAggregatedSuccessfully() {
        // given
        addTemperature(repository, EPOCH_1, TEMP_1);
        addTemperature(repository, EPOCH_2, TEMP_2);
        addTemperature(repository, EPOCH_3, TEMP_3);
        addTemperature(repository, EPOCH_4, TEMP_4); // different day

        // when
        final var hourlyAggregatedTwoHours = repository.findHourlyAggregatedData(EPOCH_3, EPOCH_1);
        final var dailyAggregatedTwoDays = repository.findDailyAggregatedData(EPOCH_1, EPOCH_4);

        //then
        assertNotNull(hourlyAggregatedTwoHours);
        assertEquals(2, hourlyAggregatedTwoHours.size());
        assertSingleResult(EPOCH_1, TEMP_1, TEMP_2, (TEMP_1 + TEMP_2) / 2, hourlyAggregatedTwoHours.get(0), ONE_HOUR_IN_SEC);
        assertSingleResult(EPOCH_3, TEMP_3, TEMP_3, (TEMP_3 + TEMP_3) / 2, hourlyAggregatedTwoHours.get(1), ONE_HOUR_IN_SEC);

        assertEquals(2, dailyAggregatedTwoDays.size());
        assertSingleResult(EPOCH_1, TEMP_3, TEMP_2, (TEMP_3 + TEMP_2) / 2, dailyAggregatedTwoDays.get(0), ONE_DAY_IN_SEC);
        assertSingleResult(EPOCH_4, TEMP_4, TEMP_4, (TEMP_4 + TEMP_4) / 2, dailyAggregatedTwoDays.get(1), ONE_DAY_IN_SEC);
    }


}