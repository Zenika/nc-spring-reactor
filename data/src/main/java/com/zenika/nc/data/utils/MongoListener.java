package com.zenika.nc.data.utils;

import com.zenika.nc.data.model.Temperature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

@Component
@Slf4j
public class MongoListener extends AbstractMongoEventListener<Temperature> {

    private final FluxSink<Temperature> senderIncoming;

    public MongoListener(FluxSink<Temperature> senderIncoming) {
        this.senderIncoming = senderIncoming;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Temperature> event) {
        log.info("onAfterSave event: " + event.toString());
        senderIncoming.next(event.getSource());
        super.onAfterSave(event);
    }
}
