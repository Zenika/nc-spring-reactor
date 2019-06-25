package com.zenika.nc.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {
    private Float value;
    private Date date;
    private Unit unit;

    public enum Unit {
        Fahrenheit, Celsius;
    }
}
