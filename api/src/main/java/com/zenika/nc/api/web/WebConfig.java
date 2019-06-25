package com.zenika.nc.api.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	@Autowired
	private TemperatureHandler temperatureHandler;

	@Bean
	public RouterFunction<?> routerFunctionTemperature() {
		return route()
				.path("/temperature", builder ->
						builder.nest(//
									 accept(MediaType.ALL), builder2 -> //
											 builder2//
													 .GET("/last", request -> temperatureHandler.getLastTemperatureData())
													 .GET("/lasts", request -> temperatureHandler.getLastTemperatureDatas())
													 .build()
						))
				.before(this::logRequest)
				.build();
	}

	@Bean
	public RouterFunction<?> routerSSE() {
		return route()
				.GET("/temperature-event", request -> temperatureHandler.getTemperatureData())
				.build();
	}

	private ServerRequest logRequest(ServerRequest request) {
		log.info(request.toString());
		return request;
	}

}