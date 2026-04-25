package com.example.agriTech.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.agriTech.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long>{
    // Tìm các thông báo chưa đọc của một thiết bị (để App Kotlin lấy)
    List<Notification> findByDeviceIdAndIsReadFalse(Long deviceId);

    // Kiểm tra xem trong khoảng thời gian gần đây đã có thông báo nào tương tự chưa
    // Cái này dùng để chặn việc spam thông báo vào DB
    boolean existsByDeviceIdAndTitleAndCreatedAtAfter(Long deviceId, String title, LocalDateTime time);

    @Query("SELECT COUNT(n) FROM Notification n " +
       "JOIN Device d ON n.device.id = d.id " +
       "JOIN PlantUser pu ON pu.device.id = d.id " +
       "WHERE pu.id = :plantUserId AND n.isRead = false")
Long countUnreadByPlantUserId(@Param("plantUserId") Long plantUserId);

@Query("SELECT n FROM Notification n " +
       "JOIN Device d ON n.device.id = d.id " +
       "JOIN PlantUser pu ON pu.device.id = d.id " +
       "WHERE pu.id = :plantUserId " +
       "ORDER BY n.createdAt DESC")
List<Notification> findAllByPlantUserId(@Param("plantUserId") Long plantUserId);


// Thêm câu này vào NotificationRepository
@Query("SELECT COUNT(n) FROM Notification n " +
       "JOIN Device d ON n.device.id = d.id " +
       "JOIN PlantUser pu ON pu.device.id = d.id " +
       "WHERE pu.user.id = :userId AND n.isRead = false")
Long countTotalUnreadByUserId(@Param("userId") Long userId);
}
