package com.zenika.nc.api.web;

import com.zenika.nc.api.model.Temperature;
import com.zenika.nc.api.service.TemperatureService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TemperatureHandler {

	private TemperatureService temperatureService;

	public TemperatureHandler(TemperatureService temperatureService) {
		this.temperatureService = temperatureService;
	}

	public Mono<ServerResponse> getLastTemperatureData() {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(temperatureService.getLastTemperatureData(), Temperature.class);
	}

	public Mono<ServerResponse> getLastTemperatureDatas() {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(temperatureService.getLastTemperatureDatas(), Temperature.class);
	}

	public Mono<ServerResponse> getTemperatureData() {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(temperatureService.generateTemperatureData(),Temperature.class);
	}
}
