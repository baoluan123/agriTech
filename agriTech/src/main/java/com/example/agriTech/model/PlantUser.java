package com.example.agriTech.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plants_user")
@Data
public class PlantUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "custom_name")
    private String customName;
    @Column(name = "last_watered")
    private LocalDateTime lastWatered;
    @Column(name = "status") 
    private Boolean status;

    //**** dto,mapping,kotlin*/
    @OneToOne
    @JoinColumn(name = "device_id")
    private Device device; // Thiết bị nào đang quản lý cây này
    
}