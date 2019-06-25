package com.zenika.nc.api.web;

import com.zenika.nc.api.model.Temperature;
import com.zenika.nc.api.service.TemperatureService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(TemperatureHandler.class)
@ContextConfiguration(classes = {
        TemperatureHandler.class,
        TemperatureService.class,
        WebConfig.class
})
public class TemperatureHandlerTest {

    @MockBean
    private TemperatureService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void should_getLastData() {
        Temperature temperature = new Temperature(15.9F, new Date(), Temperature.Unit.Celsius);

        Mockito.when(service.getLastTemperatureData())
                .thenReturn(Mono.just(temperature));

        webTestClient.get().uri("/temperature/last")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Temperature.class)
                .isEqualTo(temperature);
    }

    @Test
    public void shouldNot_getLastData() {

        Mockito.when(service.getLastTemperatureDatas())
                .thenReturn(Flux.empty());

        webTestClient.get().uri("/temperature/lasts")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getLastDatas() {

        Mockito.when(service.generateTemperatureData())
                .thenReturn(Flux.interval(Duration.ofMillis(5))
                        .onBackpressureDrop()
                        .map(it -> new Temperature()));

        List<Temperature> temperatureDataList = webTestClient.get().uri("/temperature-event")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Temperature.class)
                .getResponseBody()
                .take(3)
                .collectList()
                .block();

        Assert.assertEquals(3, temperatureDataList.size());
    }
}
