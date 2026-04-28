package com.example.agriTech.dto.sensor;

import lombok.Data;

@Data
public class SensorDTOK {
    private String sensorId;
    private int soilMoisture;
    private String recordedAt;
    
}
