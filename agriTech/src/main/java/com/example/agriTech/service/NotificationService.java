package com.example.agriTech.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.agriTech.dto.sensor.NotificationDTO;
import com.example.agriTech.mapper.device.NotificationMapper;
import com.example.agriTech.model.Notification;
import com.example.agriTech.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    //"Danh bạ" lưu UserId -> Ống dẫn tin
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    public NotificationService(NotificationRepository notificationRepository,NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }
    //Hàm đăng ký (Dùng cho Chức năng 1) real-time
    public SseEmitter addEmitter(Long UserId) {
        // Timeout 30 phút, Android sẽ tự reconnect khi hết hạn
        SseEmitter emitterd = new SseEmitter(1800000L);
        this.emitters.put(UserId, emitterd);
        // Các callback để dọn dẹp khi kết nối đóng/lỗi
        emitterd.onCompletion(()->this.emitters.remove(UserId));
        emitterd.onTimeout(()->this.emitters.remove(UserId));
        emitterd.onError((e)->this.emitters.remove(UserId));
        return emitterd;
    }
    // Hàm nội bộ: Gọi khi Sensor báo héo để đẩy tin ngay lập tức de sensor goi khi kho ***
    // 2. Đẩy tin nhắn Real-time khi Sensor báo về
    public void sendToUser(Long userId,Notification entity) {
        //** */
        SseEmitter emitterw = this.emitters.get(userId);
        if(emitterw != null) {
            try {
               NotificationDTO dto = this.notificationMapper.EntitytoDTO(entity);
               emitterw.send(SseEmitter.event().name("notification").data(dto));
            } catch (IOException e) {
                this.emitters.remove(userId);
            }
        }
    }
    // 3. Lấy lịch sử theo từng chậu cây (Dùng cho DetailFragment)
    public List<NotificationDTO> getHistory(Long plantUserId) {
        return this.notificationRepository.findByPlantUserIdOrderByCreatedAtDesc(plantUserId)
        .stream()
        .map(notificationMapper::EntitytoDTO)
        .collect(Collectors.toList());
    }
    // 5. Đánh dấu đã đọc 1 tin lẻ (Khi User click vào 1 tin)
    @Transactional
    public void markAsRead(Long id) {
        this.notificationRepository.findById(id).ifPresent(
            n->{
                n.setIsRead(true);;
                this.notificationRepository.save(n);
            }
        );
    }
    // 4. Đếm TỔNG chưa đọc của User (Dùng cho Polling ở MainActivity)
    public int getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    // 6. Đánh dấu đọc HẾT (Khi User nhấn "Đóng" Dialog ở MainActivity)
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadList = notificationRepository.findAllByUserIdAndIsReadFalse(userId);
        unreadList.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unreadList);
    }
   
    
}



//  // Đếm thông báo chưa đọc
//     public Long countUnRead(Long plantUserId) {
//         return notificationRepository.countUnreadByPlantUserId(plantUserId);
//     }

//     public List<Notification> getHistory(Long plantUserId) {
//         return notificationRepository.findAllByPlantUserId(plantUserId);
//     }
//     // Đánh dấu đã đọc
//     @Transactional
//     public void markAsRead(Long id) {
//         notificationRepository.findById(id).ifPresent(n -> {
//             n.setIsRead(true);
//             notificationRepository.save(n);
//         });
//     }

//     //them ham nay
//     public Long countTotalUnRead(Long userId) {
//     return notificationRepository.countTotalUnreadByUserId(userId);
// }