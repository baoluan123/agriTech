package com.example.agriTech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.agriTech.dto.plantuser.PlantUserDTO;
import com.example.agriTech.mapper.plantuser.PlantUserMapper;
import com.example.agriTech.model.PlantUser;
import com.example.agriTech.repository.PlantUserRepository;

@Service
public class PlantUserService {
    private final PlantUserRepository plantUserRepository;
    private final PlantUserMapper plantUserMapper;
    public PlantUserService(PlantUserRepository plantUserRepository, PlantUserMapper plantUserMapper) {
        this.plantUserRepository = plantUserRepository;
        this.plantUserMapper = plantUserMapper;
    }

    public PlantUserDTO getMyPlantDetail(Long id) {
        //opition khong dung stream
        return this.plantUserRepository.findDetailById(id)
                                        .map(plantuser->this.plantUserMapper.toDTO(plantuser))
                                        .orElseThrow(() -> new RuntimeException("Không tìm thấy cây của bạn!"));
    }
    public List<PlantUserDTO> getMyPlantList(Long userId) {
    List<PlantUser> plants = plantUserRepository.findAllByUserId(userId);
    return plants.stream()
                 .map(plantUserMapper::toDTO)
                 .collect(Collectors.toList());
}
}
