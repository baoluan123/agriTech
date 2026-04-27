package com.example.agriTech.mapper.device;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.sensor.NotificationDTO;
import com.example.agriTech.model.Notification;
@Component
public class NotificationMapper {
    // Định dạng thời gian chuẩn để Kotlin nhận diện được ngay
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NotificationDTO EntitytoDTO(Notification entity) {
        if(entity == null) return null;
        NotificationDTO dto = new NotificationDTO();
       dto.setId(entity.getId());
       dto.setUserId(entity.getUser().getId());
       dto.setPlantUserId(entity.getPlantUser().getId());
       // 2. Lấy deviceCode từ đối tượng Device liên kết
        if (entity.getDevice() != null) {
            dto.setDeviceCode(entity.getDevice().getDeviceCode());
        }

        // 3. Map nội dung thông báo
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setRead(entity.getIsRead());
        // 4. Chuyển LocalDateTime sang String theo format chuẩn
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().format(formatter));
        }
        return dto;
    }
}
