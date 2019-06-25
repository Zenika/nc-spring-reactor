package com.zenika.nc.data.web;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.repository.TemperatureRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DataController {

    private final TemperatureRepository temperatureDataRepository;
    private final DataWebClient webClient;

    public DataController(TemperatureRepository temperatureDataRepository, DataWebClient webClient) {
        this.temperatureDataRepository = temperatureDataRepository;
        this.webClient = webClient;
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
}
