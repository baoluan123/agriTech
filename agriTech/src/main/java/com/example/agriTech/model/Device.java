package com.example.agriTech.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_code", unique = true, nullable = false)
    private String deviceCode; // Ví dụ: "ESP32_GARDEN_01"
    @Column(name = "status")
    private String status; //online offline
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // ngay dang ky cay vao thiet bi

}
