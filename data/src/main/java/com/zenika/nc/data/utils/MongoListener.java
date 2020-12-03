package com.zenika.nc.data.utils;

import com.zenika.nc.data.model.Temperature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class MongoListener extends AbstractMongoEventListener<Temperature> {

    private final Sinks.Many<Temperature> temperatureMulticast;

    public MongoListener(Sinks.Many<Temperature> temperatureMulticast) {
        this.temperatureMulticast = temperatureMulticast;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Temperature> event) {
        log.info("onAfterSave event: " + event.toString());
        temperatureMulticast.tryEmitNext(event.getSource());
        super.onAfterSave(event);
    }
}
