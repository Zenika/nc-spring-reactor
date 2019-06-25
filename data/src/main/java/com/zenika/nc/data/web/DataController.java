package com.zenika.nc.data.web;

import com.zenika.nc.data.model.Temperature;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class DataController {

    private final DataWebClient webClient;

    public DataController(DataWebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(path = "all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Temperature> getAll() {
        return Flux.empty();
    }

    @GetMapping(path = "limit/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Temperature> getWithLimit(@PathVariable(name = "value") Integer count) {
        return Flux.empty();
    }
}
