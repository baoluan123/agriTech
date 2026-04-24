package com.example.agriTech.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.agriTech.dto.sensor.SensorDTO;
import com.example.agriTech.model.Device;
import com.example.agriTech.model.PlantUser;
import com.example.agriTech.model.Sensor;
import com.example.agriTech.repository.DeviceRepository;
import com.example.agriTech.repository.PlantUserRepository;
import com.example.agriTech.repository.SensorRepository;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;
    private final PlantUserRepository plantUserRepository;
    private final DeviceRepository deviceRepository;
    // Biến lưu thời điểm lưu Log cuối cùng (để giãn cách 5 phút)
    private static LocalDateTime lastLogTime = LocalDateTime.MIN;
    public SensorService(SensorRepository sensorRepository,PlantUserRepository plantUserRepository,DeviceRepository deviceRepository) {
        this.sensorRepository = sensorRepository;
        this.plantUserRepository = plantUserRepository;
        this.deviceRepository = deviceRepository;
    }
    @Transactional
    public void processData(SensorDTO dto) {
        // 1. Tìm Thiết bị dựa trên deviceCode gửi từ ESP32
        Device device = this.deviceRepository.findByDeviceCode(dto.getSensorId());
        if(device == null){
            return;
        }
        // 2. Tìm Cây đang được gắn với thiết bị này
        PlantUser plantUser = this.plantUserRepository.findByDevice(device);
        // 3. CẬP NHẬT TRẠNG THÁI (Nếu có PlantUser)
        if(plantUser != null ) {
            //B1: luôn update để App Kotlin lấy được số mới nhất
            // Cập nhật lần cuối tưới nếu trạng thái là UOT
            if("UOT".equalsIgnoreCase(dto.getStatus())){
                //**** */
                plantUser.setLastWatered(LocalDateTime.now());
                this.plantUserRepository.save(plantUser);
            }

        }
        // 4. LƯU LỊCH SỬ (Giãn cách 5 phút để SQL không bị quá tải)
        if(LocalDateTime.now().isAfter(lastLogTime.plusMinutes(5))) {
            //mapping sau
            Sensor log = new Sensor();
            log.setDevice(device);
            log.setSensorId(dto.getSensorId());
            log.setTemperature(dto.getTemperature());
            log.setHumidity(dto.getHumidity());
            log.setSoilMoisture(dto.getSoilMoisture());
            log.setStatus(dto.getStatus());
            log.setRecordedAt(LocalDateTime.now());
            this.sensorRepository.save(log);
            lastLogTime = LocalDateTime.now(); // Cập nhật mốc thời gian đã lưu
            System.out.println(">>> Đã lưu Log cho thiết bị: " + dto.getSensorId());
        }                        
    }

}
