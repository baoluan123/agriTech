package com.example.agriTech.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Sensor;
@Repository
public interface SensorRepository extends JpaRepository<Sensor,Long> {
//    @Query(value = "SELECT sl.* FROM (" +
//                "  SELECT sl.* FROM sensor_logs sl " +
//                "  JOIN plants_user pu ON sl.device_id = pu.device_id " + // Kết nối qua device_id
//                "  WHERE pu.id = :pId " + // Lọc theo ID của PlantUser (cái Kotlin truyền vào)
//                "  ORDER BY sl.recorded_at DESC LIMIT (:l)" +
//                ") sub ORDER BY recorded_at ASC", nativeQuery = true)

    @Query(value = "SELECT sl.* FROM sensor_logs sl " +
               "JOIN plants_user pu ON sl.device_id = pu.device_id " +
               "WHERE pu.id = :pId " +
               "ORDER BY sl.recorded_at DESC", nativeQuery = true) 
    List<Sensor> findRecentLogs(@Param("pId") Long plantUserId, Pageable pageable);                     
//    List<Sensor> findTopHistory(@Param("pId") Long deviceId, @Param("l") int limit);
}
