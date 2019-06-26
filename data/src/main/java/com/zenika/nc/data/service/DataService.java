package com.zenika.nc.data.service;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.repository.TemperatureRepository;
import com.zenika.nc.data.utils.JacksonConverter;
import com.zenika.nc.data.web.DataWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class DataService {

	private final DataWebClient webClient;

	private final TemperatureRepository repository;

	public DataService(DataWebClient webClient, TemperatureRepository repository) {
		this.webClient = webClient;
		this.repository = repository;
	}

	@PostConstruct
	private void init() {
		// call web client route
		temperatureEventFlux()
				// subscribe to sse channel
				.subscribe(temperatureData -> System.out.println(temperatureData.toString()), Throwable::printStackTrace);
	}

	private Flux<Temperature> temperatureEventFlux() {
		return webClient.getTemperatureEvent()

				// map sse payload to temperature data
				.map(JacksonConverter::deserialize)

				// map object to value (float)
				.map(Temperature::getValue)

				// buffer values
				.buffer(10)

				// map values to average value
				.map(this::computeAverage)

				// map value to object
				.map(this::buildDocument)

				// save document in database
				.flatMap(repository::save);
	}

	public Flux<Tuple2<Temperature, Integer>> getTemperatureAndHumidity() {
		return webClient.getTemperatureEvent()
				.map(JacksonConverter::deserialize)
				.zipWith(webClient.getHumidity()
				.onErrorReturn(Integer.valueOf(0)));
	}

	private Float computeAverage(List<Float> floatList) {
		float sum = 0;
		for (Float listTemperatureData : floatList) {
			sum += listTemperatureData;
		}
		return sum / 10;
	}

	private Temperature buildDocument(Float aFloat) {
		return Temperature.builder().date(new Date()).value(aFloat).unit(Temperature.Unit.Celsius).build();
	}
}
