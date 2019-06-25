package com.zenika.nc.data.web;

import com.zenika.nc.data.model.Temperature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DataWebClient {

    private final ParameterizedTypeReference<ServerSentEvent<String>> typeReference
            = new ParameterizedTypeReference<ServerSentEvent<String>>() {
    };

    private final WebClient webClient;

    public DataWebClient(@Value("${api.url}") String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    public Flux<ServerSentEvent<String>> getTemperatureEvent() {
        return webClient.get().uri("/temperature-event")
                .retrieve()
                .bodyToFlux(typeReference);
    }

    public Flux<Temperature> getLastDatas(){
        return webClient.get().uri("/temperature/lasts")
                .retrieve()
                .bodyToFlux(Temperature.class);
    }

    public Mono<Temperature> getLastData(){
        return webClient.get().uri("/temperature/last")
                .retrieve()
                .bodyToMono(Temperature.class);
    }

}
