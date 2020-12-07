package com.zenika.nc.data.utils;

import com.zenika.nc.data.model.Temperature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class TemperatureProcessor {

    @Bean
    Sinks.Many<Temperature> temperatureMulticast() {
        return Sinks.many().multicast().directAllOrNothing();
    }

    @Bean
    Flux<Temperature> temperatureMulticastFlux() {
        return temperatureMulticast().asFlux();
    }

}
