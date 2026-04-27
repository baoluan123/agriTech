package com.example.agriTech.dto.sensor;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;            // ID của thông báo (để đánh dấu đã đọc)
    private Long userId;        // ĐỂ BIẾT GỬI CHO AI (Xử lý chức năng 1)
    private Long plantUserId;   // ĐỂ LỌC THEO CÂY (Xử lý chức năng 2)
    private String title;
    private String message;
    private boolean isRead;     // TRẠNG THÁI (Xử lý chức năng 3)
    private String createdAt;   // Thời gian để hiện ở History

    private String deviceCode;
    
}
