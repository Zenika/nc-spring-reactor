package com.zenika.nc.data.web;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.repository.TemperatureRepository;
import com.zenika.nc.data.utils.JacksonConverter;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DataController {

    private final TemperatureRepository temperatureDataRepository;
    private final DataWebClient webClient;
    private final Flux<Temperature> temperatureMulticastFlux;

    public DataController(TemperatureRepository temperatureDataRepository, DataWebClient webClient, Flux<Temperature> temperatureMulticastFlux) {
        this.temperatureDataRepository = temperatureDataRepository;
        this.webClient = webClient;
        this.temperatureMulticastFlux = temperatureMulticastFlux;
    }

    @GetMapping(path = "all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> getAll() {
        return temperatureDataRepository.findAll();
    }

    @GetMapping(path = "limit", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> getAllWithLimit() {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        return temperatureDataRepository.findAll()
                .handle((temperature, sink) -> {
                    if (atomicInteger.getAndIncrement() == 10) {
                        sink.complete();
                        return;
                    }
                    sink.next(temperature);
                });
    }

    @GetMapping(path = "event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getTemperatureEvent() {
        return temperatureMulticastFlux
                .log()
                // map object to json payload
                .map(JacksonConverter::serialize)
                // map json payload to server sent event object
                .map(this::toServerSentEvent);
    }

    private ServerSentEvent<String> toServerSentEvent(String payload) {
        return ServerSentEvent.<String>builder()
                .id(LocalTime.now().toString())
                .event("new-average-value")
                .data(payload)
                .build();
    }
}
