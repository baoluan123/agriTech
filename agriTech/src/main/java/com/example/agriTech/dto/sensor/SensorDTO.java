package com.example.agriTech.dto.sensor;

import lombok.Data;

@Data
public class SensorDTO {
    private String sensorId;
    private Double temperature;
    private Double humidity;
    private Integer soilMoisture; // khop vs ben esp
    private String status;
}
