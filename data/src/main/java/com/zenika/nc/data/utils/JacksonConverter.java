package com.zenika.nc.data.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.nc.data.model.Temperature;
import org.springframework.http.codec.ServerSentEvent;

import java.io.IOException;

public class JacksonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Temperature deserialize(ServerSentEvent<String> sseString) {
        try {
            return objectMapper.readValue(sseString.data(), Temperature.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serialize(Temperature pojo) {
        try {
            return objectMapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
