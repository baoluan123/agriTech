package com.example.agriTech.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.agriTech.model.Notification;
import com.example.agriTech.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    // Đếm thông báo chưa đọc
    public Long countUnRead(Long plantUserId) {
        return notificationRepository.countUnreadByPlantUserId(plantUserId);
    }

    public List<Notification> getHistory(Long plantUserId) {
        return notificationRepository.findAllByPlantUserId(plantUserId);
    }
    // Đánh dấu đã đọc
    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    //them ham nay
    public Long countTotalUnRead(Long userId) {
    return notificationRepository.countTotalUnreadByUserId(userId);
}
    
}
