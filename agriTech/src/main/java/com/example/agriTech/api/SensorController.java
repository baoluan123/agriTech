package com.example.agriTech.api;



import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensor")
public class SensorController {
    //import org.hibernate.mapping.Map; Map của Hibernate dùng cho cấu trúc dữ liệu database
    //import java.util.Map; kiểu Map (Key-Value) mà Spring Boot dùng để hứng dữ liệu JSON.
    @PostMapping("/moisture")
    public ResponseEntity<String> receiveMoisture(@RequestBody Map<String, Object> data) {
        System.out.println("ID Cảm biến: " + data.get("sensorId"));
        
        System.out.println("nhiệt độ: " + data.get("temperature"));
        System.out.println("Giá trị độ ẩm: " + data.get("humidity"));
        System.out.println("Giá trị độ ẩm đất: " + data.get("soilMoisture"));
        System.out.println("Trạng thái: " + data.get("status"));
        

        return ResponseEntity.ok("Server VKU đã nhận dữ liệu!");

    }
    
}
