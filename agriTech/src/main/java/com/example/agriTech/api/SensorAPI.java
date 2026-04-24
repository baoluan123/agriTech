package com.example.agriTech.api;





import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.dto.sensor.SensorDTO;
import com.example.agriTech.service.SensorService;

@RestController
@RequestMapping("/api/sensor")
public class SensorAPI {
    private final SensorService sensorService;
    public SensorAPI(SensorService sensorService) {
        this.sensorService  = sensorService;
    }
    //import org.hibernate.mapping.Map; Map của Hibernate dùng cho cấu trúc dữ liệu database
    //import java.util.Map; kiểu Map (Key-Value) mà Spring Boot dùng để hứng dữ liệu JSON.
    //Map<String, Object>
    @PostMapping("/moisture")
    public ResponseEntity<String> receiveMoisture(@RequestBody SensorDTO data) {
        //anh xa model -> dto bx sau chuyen thanh file mapping
        // Sensor sensor = new Sensor();
        // sensor.setSensorId(data.getSensorId());
        // sensor.setHumidity(data.getHumidity());
        // sensor.setTemperature(data.getTemperature());
        // sensor.setSoilMoisture(data.getValue());
        // sensor.setStatus(data.getStatus());

        // // luu sql
        // this.sensorRepository.save(sensor);
        // System.out.println("Đã lưu dữ liệu từ: " + data.getSensorId());

        // System.out.println("ID Cảm biến: " + data.get("sensorId"));
        
        // System.out.println("nhiệt độ: " + data.get("temperature"));
        // System.out.println("Giá trị độ ẩm: " + data.get("humidity"));
        // System.out.println("Giá trị độ ẩm đất: " + data.get("soilMoisture"));
        // System.out.println("Trạng thái: " + data.get("status"));
        this.sensorService.processData(data);
        return ResponseEntity.ok("Server VKU đã nhận dữ liệu!");

    }
    
}
