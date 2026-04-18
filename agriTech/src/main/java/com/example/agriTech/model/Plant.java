package com.example.agriTech.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType; // Cần cái này để dùng CascadeType.ALL

@Entity
@Table(name = "plants")
@Data

public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_plant")
    private String namePlant;
    @Column(name ="ideal_humidity")
    private Float idealHumidity;
    @Column(name ="water_frequency")
    private Integer waterFrequency;
    @Column(name = "fertilizer_info")
    private String fertilizerInfo;
    @Column(name = "description_plant")
    private String descriptionPlant;
    @JsonManagedReference
    @OneToMany(mappedBy = "plant",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Img> images;
   
}
