package com.zenika.nc.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import org.springframework.ui.Model;

@Controller
public class FrontController {

    @Autowired
    private FrontWebClient webClient;

    @GetMapping
    public Mono<String> temperature(Model model, ServerWebExchange serverWebExchange) {
        model.addAttribute("temperatures", new ReactiveDataDriverContextVariable(webClient.getTemperatureEvent()));
        return Mono.just("index");
    }

}
