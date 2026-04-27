package com.example.agriTech.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.agriTech.dto.sensor.NotificationDTO;

import com.example.agriTech.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApi {
    private final NotificationService notificationService;
    public NotificationApi(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 1. Chức năng Real-time: Đăng ký nhận tin theo UserId
    // App Kotlin sẽ gọi cái này ngay sau khi Login thành công
    @GetMapping(value = "/subscribe/{userId}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId){
        return this.notificationService.addEmitter(userId);
    }
    // 2. Chức năng History: Lấy lịch sử theo plantUserId
    // Dùng để hiện danh sách thông báo khi bấm vào chi tiết một cái cây
    @GetMapping("/history/{plantUserId}")
    public ResponseEntity<List<NotificationDTO>> getHistory(@PathVariable Long plantUserId) {
        List<NotificationDTO> nDtos = this.notificationService.getHistory(plantUserId);
        return ResponseEntity.ok(nDtos);

    }
    // 3. Chức năng Mark as Read: Đánh dấu đã đọc
    // Khi người dùng nhấn vào thông báo trên điện thoại
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        this.notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    // --- 2 HÀM MỚI THÊM ĐỂ KHỚP VỚI MAIN ACTIVITY ANDROID ---

    // 4. Lấy TỔNG số tin chưa đọc của User (Cho cái vòng lặp 10s check Dialog)
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Long userId) {
        int count = this.notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
    // 5. Đánh dấu đọc HẾT cho User (Khi nhấn nút "Đóng" Dialog)
    @PatchMapping("/mark-all-read/{userId}")
    public ResponseEntity<Void> markAllRead(@PathVariable Long userId) {
        this.notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

}


// //     @GetMapping("plant-user/{plantUserId}/unread-count")
// //     public ResponseEntity<Long> getUnRead(@PathVariable Long plantUserId) {
// //         return ResponseEntity.ok(this.notificationService.countUnRead(plantUserId));
// //     }
// //     @GetMapping("/plant-user/{plantUserId}")
// //     public ResponseEntity<List<Notification>> getAll(@PathVariable Long plantUserId) {
// //         return ResponseEntity.ok(this.notificationService.getHistory(plantUserId));
// //     }
// //     @PatchMapping("/{id}/read")
// //     public ResponseEntity<String> read(@PathVariable Long id) {
// //         this.notificationService.markAsRead(id);
// //         return ResponseEntity.ok("Success");
// //     }

// //     //them ham nay
// //     @GetMapping("/user/{userId}/unread-count")
// // public ResponseEntity<Long> getTotalUnRead(@PathVariable Long userId) {
// //     return ResponseEntity.ok(this.notificationService.countTotalUnRead(userId));
// // }
