package com.zenika.nc.data.repository;

import com.zenika.nc.data.model.Temperature;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TemperatureRepository extends ReactiveMongoRepository<Temperature, String> {

}
