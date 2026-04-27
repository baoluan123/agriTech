package com.example.agriTech.repository;

import java.time.LocalDateTime;
import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{
//         // Kiểm tra xem trong khoảng thời gian gần đây đã có thông báo nào tương tự chưa
//     // Cái này dùng để chặn việc spam thông báo vào DB
    boolean existsByDeviceIdAndTitleAndCreatedAtAfter(Long deviceId, String title, LocalDateTime time);

    // 2. Lấy lịch sử theo Cây (Dùng cho Chức năng 2)
    // Kotlin gửi plantUserId lên, mình trả về danh sách sắp xếp mới nhất lên đầu
    List<Notification> findByPlantUserIdOrderByCreatedAtDesc(Long plantUserId);
    // 3. Đếm số tin chưa đọc của một User (Dùng cho Chức năng 1 - Real-time)
    // Để hiển thị con số (badge) trên cái chuông ở Android
    int countByUserIdAndIsReadFalse(Long userId);
    // 4. Lấy tất cả thông báo của 1 User (Nếu sau này ông làm màn hình Thông báo tổng)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    // Tìm tất cả thông báo chưa đọc của user để update
    List<Notification> findAllByUserIdAndIsReadFalse(Long userId);
}







  // Tìm các thông báo chưa đọc của một thiết bị (để App Kotlin lấy)
//     List<Notification> findByDeviceIdAndIsReadFalse(Long deviceId);



//     @Query("SELECT COUNT(n) FROM Notification n " +
//        "JOIN Device d ON n.device.id = d.id " +
//        "JOIN PlantUser pu ON pu.device.id = d.id " +
//        "WHERE pu.id = :plantUserId AND n.isRead = false")
// Long countUnreadByPlantUserId(@Param("plantUserId") Long plantUserId);

// @Query("SELECT n FROM Notification n " +
//        "JOIN Device d ON n.device.id = d.id " +
//        "JOIN PlantUser pu ON pu.device.id = d.id " +
//        "WHERE pu.id = :plantUserId " +
//        "ORDER BY n.createdAt DESC")
// List<Notification> findAllByPlantUserId(@Param("plantUserId") Long plantUserId);


// // Thêm câu này vào NotificationRepository
// @Query("SELECT COUNT(n) FROM Notification n " +
//        "JOIN Device d ON n.device.id = d.id " +
//        "JOIN PlantUser pu ON pu.device.id = d.id " +
//        "WHERE pu.user.id = :userId AND n.isRead = false")
// Long countTotalUnreadByUserId(@Param("userId") Long userId);
