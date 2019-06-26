package com.zenika.nc.data.service;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.repository.TemperatureRepository;
import com.zenika.nc.data.utils.JacksonConverter;
import com.zenika.nc.data.web.DataWebClient;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class DataServiceTest {

	@Mock
	DataWebClient dataWebClient;

	@Mock
	TemperatureRepository temperatureRepository;

	DataService dataService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		dataService = new DataService(dataWebClient, temperatureRepository);
	}

	@Test
	public void getTemperatureAndHumidity_should_return_both_values() {

		Mockito.when(dataWebClient.getTemperatureEvent()).thenReturn(
				Flux.just(ServerSentEvent.<String>builder().data(JacksonConverter.serialize(Temperature.builder().value(20F).build())).build()));
		Mockito.when(dataWebClient.getHumidity()).thenReturn(Flux.just(10));

		StepVerifier.create(dataService.getTemperatureAndHumidity())
				.consumeNextWith(tupple->{
					Assertions.assertThat(tupple.getT1().getValue()).isEqualTo(20F);
					Assertions.assertThat(tupple.getT2()).isEqualTo(10);
				})
				.verifyComplete();

	}

	@Test
	public void getTemperatureAndHumidity_should_return_humidity_equal_0_when_error() {

		Mockito.when(dataWebClient.getTemperatureEvent())
				.thenReturn(Flux.just(ServerSentEvent.<String>builder().data(JacksonConverter.serialize(new Temperature())).build()));
		Mockito.when(dataWebClient.getHumidity()).thenReturn(Flux.error(new Exception("error on getting humidity")));

		StepVerifier.create(dataService.getTemperatureAndHumidity()).consumeNextWith(tupple -> Assertions.assertThat(tupple.getT2()).isEqualTo(0))
				.verifyComplete();

	}

	@Test
	public void getTemperatureAndHumidity_should_return_error_when_temperature_error() {

		Mockito.when(dataWebClient.getTemperatureEvent()).thenReturn(Flux.error(new Exception("no temperature")));
		Mockito.when(dataWebClient.getHumidity()).thenReturn(Flux.just(10));

		StepVerifier.create(dataService.getTemperatureAndHumidity()).expectError().verify();

	}

}