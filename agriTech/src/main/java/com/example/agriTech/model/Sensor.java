package com.example.agriTech.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sensor_logs")
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sensor_id")
    private String sensorId;
    //DHT11
    @Column(name = "temperature") 
    private Double temperature; // nhiet do
    @Column(name = "humidity")
    private Double humidity;
    //do am dat
    @Column(name = "soil_moisture")
    private Integer soilMoisture; // do am dat
    @Column(name = "status")
    private String status; // "KHO" hoặc "UOT"
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt = LocalDateTime.now(); // du lieu gui lien tuc

   

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    
}
