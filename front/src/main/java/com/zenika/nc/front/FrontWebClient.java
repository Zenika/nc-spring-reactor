package com.zenika.nc.front;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class FrontWebClient {

    private final ParameterizedTypeReference<ServerSentEvent<String>> type
            = new ParameterizedTypeReference<ServerSentEvent<String>>() {
    };

    private WebClient webClient;

    public FrontWebClient() {
        webClient = WebClient.create("http://localhost:8082");
    }

    Flux<Temperature> getTemperatureEvent() {
        return webClient.get().uri("/event")
                .retrieve()
                .bodyToFlux(type)
                .map(JacksonConverter::deserialize);
    }

}
