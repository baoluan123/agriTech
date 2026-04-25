package com.example.agriTech.api;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.model.Notification;
import com.example.agriTech.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApi {
    private final NotificationService notificationService;
    public NotificationApi(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("plant-user/{plantUserId}/unread-count")
    public ResponseEntity<Long> getUnRead(@PathVariable Long plantUserId) {
        return ResponseEntity.ok(this.notificationService.countUnRead(plantUserId));
    }
    @GetMapping("/plant-user/{plantUserId}")
    public ResponseEntity<List<Notification>> getAll(@PathVariable Long plantUserId) {
        return ResponseEntity.ok(this.notificationService.getHistory(plantUserId));
    }
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> read(@PathVariable Long id) {
        this.notificationService.markAsRead(id);
        return ResponseEntity.ok("Success");
    }

    //them ham nay
    @GetMapping("/user/{userId}/unread-count")
public ResponseEntity<Long> getTotalUnRead(@PathVariable Long userId) {
    return ResponseEntity.ok(this.notificationService.countTotalUnRead(userId));
}
}
