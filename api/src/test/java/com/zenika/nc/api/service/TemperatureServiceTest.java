package com.zenika.nc.api.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.zenika.nc.api.service.TemperatureService.LAST_TEMPERATURE;
import static com.zenika.nc.api.service.TemperatureService.LAST_TEMPERATURES;
import static com.zenika.nc.api.service.TemperatureService.toFahrenheit;

public class TemperatureServiceTest {

    private TemperatureService temperatureService;

    @Before
    public void setUp() {
        temperatureService = new TemperatureService();
    }

    @Test
    public void getLastTemperatureData_should_emit_one_value() {
        StepVerifier.create(temperatureService.getLastTemperatureData())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getLastTemperatureData_should_emit_LAST_TEMPERATURE_in_farenheit() {
        StepVerifier.create(temperatureService.getLastTemperatureData())
                .consumeNextWith(aTemperature->
                                         Assertions.assertThat(aTemperature.getValue())
                                                 .isEqualTo(toFahrenheit(LAST_TEMPERATURE)))
                .verifyComplete();
    }

    @Test
    public void getLastTemperatureDatas_should_emit_all_temperatures() {
        StepVerifier.create(temperatureService.getLastTemperatureDatas())
                .consumeNextWith(temperatureData -> Assertions.assertThat(temperatureData.getValue()).isEqualTo(LAST_TEMPERATURES[0]))
                .consumeNextWith(temperatureData -> Assertions.assertThat(temperatureData.getValue()).isEqualTo(LAST_TEMPERATURES[1]))
                .consumeNextWith(temperatureData -> Assertions.assertThat(temperatureData.getValue()).isEqualTo(LAST_TEMPERATURES[2]))
                .consumeNextWith(temperatureData -> Assertions.assertThat(temperatureData.getValue()).isEqualTo(LAST_TEMPERATURES[3]))
                .consumeNextWith(temperatureData -> Assertions.assertThat(temperatureData.getValue()).isEqualTo(LAST_TEMPERATURES[4]))
                .verifyComplete();
    }

    @Test
    public void generateTemperatureData_should_emit_value_every_100ms(){

        StepVerifier.withVirtualTime(()->temperatureService.generateTemperatureData())
                .expectSubscription()
                .thenAwait(Duration.ofMillis(500))
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }

}
