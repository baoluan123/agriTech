package com.example.agriTech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.agriTech.dto.plantuser.PlantUserDTO;
import com.example.agriTech.mapper.plantuser.PlantUserMapper;
import com.example.agriTech.model.Device;
import com.example.agriTech.model.Plant;
import com.example.agriTech.model.PlantUser;
import com.example.agriTech.model.User;
import com.example.agriTech.repository.DeviceRepository;
import com.example.agriTech.repository.PlantRepository;
import com.example.agriTech.repository.PlantUserRepository;
import com.example.agriTech.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PlantUserService {
    private final PlantUserRepository plantUserRepository;
    private final PlantUserMapper plantUserMapper;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    public PlantUserService(PlantUserRepository plantUserRepository, PlantUserMapper plantUserMapper,PlantRepository plantRepository,UserRepository userRepository,DeviceRepository deviceRepository) {
        this.plantUserRepository = plantUserRepository;
        this.plantUserMapper = plantUserMapper;

        this.deviceRepository = deviceRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
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
    @Transactional
    public PlantUserDTO addPlantUser(PlantUserDTO dto) {
        System.out.println("DEBUG: plantId=" + dto.getPlantId() + ", userId=" + dto.getUserId() + ", device=" + dto.getDeviceCode());
        // 1. Tìm các thực thể liên quan
        Plant plant = this.plantRepository.findById(dto.getPlantId())
        .orElseThrow(() -> new RuntimeException("Không tìm thấy giống cây!"));
        User user = this.userRepository.findByAccountId(dto.getUserId())
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        // Tìm Device dựa trên DeviceCode từ Kotlin gửi sang
        Device device = this.deviceRepository.findByDeviceCode(dto.getDeviceCode());
        if(device == null) {
            throw new RuntimeException("Thiết bị này chưa được đăng ký trong hệ thống!");
        }
        // 2. Tạo Entity và gán dữ liệu
        PlantUser plantUser = new PlantUser();
        plantUser.setPlant(plant);
        plantUser.setUser(user);
        plantUser.setDevice(device);
        // Nếu Kotlin không gửi customName, lấy tên mặc định của giống cây
        plantUser.setCustomName(dto.getCustomName() != null ? dto.getCustomName() : plant.getNamePlant());
        plantUser.setLastWatered(dto.getLastWatered()); // Chuỗi String/LocalDateTime
        plantUser.setStatus(true);
        // 3. Lưu vào MySQL
        PlantUser saved = this.plantUserRepository.save(plantUser);
        // 4. Trả về DTO đã "bơm" đầy đủ thông tin để Kotlin hiện UI
        return this.plantUserMapper.toDTO(saved);

    }

}
