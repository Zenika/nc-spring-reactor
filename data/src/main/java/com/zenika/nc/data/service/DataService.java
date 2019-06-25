package com.zenika.nc.data.service;

import com.zenika.nc.data.model.Temperature;
import com.zenika.nc.data.web.DataWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class DataService {

    private final DataWebClient webClient;

    public DataService(DataWebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    private void init() {
        // call web client route
        temperatureEventFlux()
                // subscribe to sse channel
                .subscribe(temperatureData -> System.out.println(temperatureData.toString()), Throwable::printStackTrace);
    }

    private Flux<Temperature> temperatureEventFlux() {
        return Flux.empty();
    }

    private Float computeAverage(List<Float> floatList) {
        float sum = 0;
        for (Float listTemperatureData : floatList) {
            sum += listTemperatureData;
        }
        return sum / 10;
    }

    private Temperature buildDocument(Float aFloat) {
        return Temperature.builder()
                .date(new Date())
                .value(aFloat)
                .unit(Temperature.Unit.Celsius)
                .build();
    }
}
