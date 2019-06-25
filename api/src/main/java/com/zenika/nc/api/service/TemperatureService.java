package com.zenika.nc.api.service;

import com.zenika.nc.api.model.Temperature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

@Component
class TemperatureService {

    /**
     * simulation des températures renvoyé par le capteur
     */
    static final Float[] LAST_TEMPERATURES = new Float[]{20.9F, 21.5F, 22.1F, 21.5F, 20.7F};

    /**
     * simulation d'une température renvoyé par le capteur
     */
    static final Float LAST_TEMPERATURE = 20.7F;


    /**
     * TP1
     * La méthode doit retourner un la valeur dans LAST_TEMPERATURE sous forme d'un Mono
     */
    Mono<Float> getLastTemperatureAsFloat(){
        return Mono.empty();
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Mono<Temperature> getLastTemperatureData() {
        return Mono.empty();
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Flux<Temperature> getLastTemperatureDatas() {
        return Flux.empty();
    }

    /**
     * TP 1 : implémenter la méthode
     */
    Flux<Temperature> generateTemperatureData() {
        return Flux.empty();
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
