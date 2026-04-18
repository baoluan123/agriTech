package com.example.agriTech.model;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "plant_images")
@Data
public class Img {
     @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id; 
   @JsonBackReference
   @ManyToOne
   @JoinColumn(name = "plant_id")
   private Plant plant;
   @Column(name = "url_img")
   private String urlImg;
   @Column(name = "is_thumbnail")
   private Boolean isThumbnail;
    
}
