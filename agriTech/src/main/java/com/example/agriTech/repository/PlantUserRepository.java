package com.example.agriTech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Device;
import com.example.agriTech.model.PlantUser;
@Repository
public interface PlantUserRepository extends JpaRepository<PlantUser, Long> {

    @Query("SELECT pu FROM PlantUser pu " +
       "JOIN FETCH pu.plant p " +
       "LEFT JOIN FETCH p.images " +
       "JOIN FETCH pu.user u " +
       "JOIN FETCH u.account " +
       "WHERE pu.id = :id")
    Optional<PlantUser> findDetailById(@Param("id") Long id);
// Thêm hàm này để lấy danh sách cây của 1 User cụ thể cho màn hình MyPlant
    @Query("SELECT pu FROM PlantUser pu " +
           "JOIN FETCH pu.plant p " +
           "LEFT JOIN FETCH p.images " +
           "WHERE pu.user.id = :userId")
    List<PlantUser> findAllByUserId(@Param("userId") Long userId);

    PlantUser findByDevice(Device device);

    // Cách 1: Viết theo đúng chuẩn Property Traversal (Spring tự sinh SQL)
    Optional<PlantUser> findByDeviceDeviceCode(String deviceCode);

    // Cách 2: Nếu bạn muốn an toàn, tránh nhầm lẫn giữa các thuộc tính trùng tên
    // Dùng dấu gạch dưới "_" để phân tách giữa các Entity
    Optional<PlantUser> findByDevice_DeviceCode(String deviceCode);
}
