package com.zenika.nc.data.web;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.repository.TemperatureRepository;
import com.zenika.nc.data.utils.JacksonConverter;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.DirectProcessor;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DataController {

    private final TemperatureRepository temperatureDataRepository;
    private final DirectProcessor<Temperature> senderProcessor;

    public DataController(TemperatureRepository temperatureDataRepository, DirectProcessor<Temperature> senderProcessor) {
        this.temperatureDataRepository = temperatureDataRepository;
        this.senderProcessor = senderProcessor;
    }

    @GetMapping(path = "all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> getAll() {
        return temperatureDataRepository.findAll();
    }

    @GetMapping(path = "limit/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Temperature> getWithLimit(@PathVariable(name = "value") Integer count) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        return temperatureDataRepository.findAll()
                .handle((temperature, sink) -> {
                    if (atomicInteger.getAndIncrement() == count) {
                        sink.complete();
                        return;
                    }
                    sink.next(temperature);
                });
    }

    @GetMapping(path = "event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getTemperatureEvent() {
        return senderProcessor
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
