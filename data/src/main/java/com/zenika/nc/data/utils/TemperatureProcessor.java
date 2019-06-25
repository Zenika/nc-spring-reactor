package com.zenika.nc.data.utils;

import com.zenika.nc.data.model.Temperature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Configuration
public class TemperatureProcessor {

    @Bean
    DirectProcessor<Temperature> senderProcessor() {
        return DirectProcessor.create();
    }

    @Bean
    FluxSink<Temperature> senderIncoming() {
        return senderProcessor().sink();
    }

}
