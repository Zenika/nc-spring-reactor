package com.zenika.nc.data.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = {
        DataWebClient.class
})
public class DataWebClientTest {

    @Value("${api.url}")
    private String baseUrl;

    private DataWebClient dataWebClient;

    @Before
    public void setup(){
        this.dataWebClient=new DataWebClient(baseUrl);
    }


    @Test
    public void getLastData_should_return_mono_Temperature(){
        StepVerifier.create(dataWebClient.getLastData())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getLastData_should_return_flux_of_Temperature(){
        StepVerifier.create(dataWebClient.getLastDatas())
                .thenRequest(1)
                .expectNextCount(1)
                .thenRequest(1)
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }

    @Test
    public void getTemperatureEvent_should_return_ServerSentEvent(){
        StepVerifier.create(dataWebClient.getTemperatureEvent())
                .thenRequest(2)
                .expectNextCount(2)
                .thenCancel()
                .verify();
    }


}