package com.zenika.nc.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {

    @Id
    @JsonIgnore
    private ObjectId id;

    private Float value;
    private Date date;
    private Unit unit;

    public enum Unit {
        Fahrenheit, Celsius;
    }
}
