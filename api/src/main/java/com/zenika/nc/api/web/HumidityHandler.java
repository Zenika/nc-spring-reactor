package com.zenika.nc.api.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Random;

@RestController
@RequestMapping(path="/humidity")
public class HumidityHandler {

	@GetMapping
	public Flux<Integer> getHumidity(){
		return Flux.just(1,2,3,4,5)
				.map(nb->generateFloat());
	}

	private final Random random = new Random();

	private int generateFloat() {
		final int min = 0;
		final int max = 100;
		return min + random.nextInt() * (max - min);
	}


}
