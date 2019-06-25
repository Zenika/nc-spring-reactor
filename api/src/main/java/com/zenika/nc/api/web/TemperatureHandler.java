package com.zenika.nc.api.web;

import com.zenika.nc.api.model.Temperature;
import com.zenika.nc.api.service.TemperatureService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TemperatureHandler {

	private TemperatureService temperatureService;

	public TemperatureHandler(TemperatureService temperatureService) {
		this.temperatureService = temperatureService;
	}

	@GetMapping(path = "/temperature/last")
	public Mono<Temperature> getLastTemperatureData() {
		return temperatureService.getLastTemperatureData();
	}

	@GetMapping(path = "/temperature/lasts")
	public Flux<Temperature> getLastTemperatureDatas() {
		return temperatureService.getLastTemperatureDatas();
	}

	@GetMapping(path = "/temperature-event",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Temperature> getTemperatureData() {
		return temperatureService.generateTemperatureData();
	}
}
