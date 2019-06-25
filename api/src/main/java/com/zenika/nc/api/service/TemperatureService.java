package com.zenika.nc.api.service;

import com.zenika.nc.api.model.Temperature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

@Component
class TemperatureService {

    /**
     * TP 1 : simulation des températures renvoyé par le capteur
     */
    static final Float[] LAST_TEMPERATURES = new Float[]{20.9F, 21.5F, 22.1F, 21.5F, 20.7F};

    /**
     * TP 1 : simulation d'une température renvoyé par le capteur
     */
    static final Float LAST_TEMPERATURE = 20.7F;

    /**
     * TP1 : implémenter la méthode
     */
    public Mono<Float> getLastTemperatureAsFloat(){
        return Mono.just(LAST_TEMPERATURE);
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Mono<Temperature> getLastTemperatureData() {
        return getLastTemperatureAsFloat()
                .map(TemperatureService::toFahrenheit)
                .map(aFloat -> new Temperature(aFloat, new Date(), Temperature.Unit.Fahrenheit));
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Flux<Temperature> getLastTemperatureDatas() {
        return Flux.fromArray(LAST_TEMPERATURES)
                .map(aFloat -> new Temperature(aFloat, new Date(), Temperature.Unit.Celsius));
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Flux<Temperature> generateTemperatureData() {
        return Flux.interval(Duration.ofMillis(100))
                .onBackpressureDrop()
                .map(aLong -> generateFloat())
                .map(aFloat -> new Temperature(aFloat, new Date(), Temperature.Unit.Celsius));
    }

    /**
     * TP 1
     */
    static float toFahrenheit(float celsiusTemperature) {
        return celsiusTemperature * (9f / 5) + 32;
    }

    private final Random random = new Random();

    private float generateFloat() {
        final float min = 18.0F;
        final float max = 25.0F;
        return min + random.nextFloat() * (max - min);
    }
}
