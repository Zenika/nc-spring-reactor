package com.zenika.nc.data.utils;

import com.zenika.nc.data.model.Temperature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoListener extends AbstractMongoEventListener<Temperature> {

    @Override
    public void onAfterSave(AfterSaveEvent<Temperature> event) {
        log.info("onAfterSave event: " + event.toString());
        super.onAfterSave(event);
    }
}
