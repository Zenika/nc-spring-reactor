package com.zenika.nc.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
