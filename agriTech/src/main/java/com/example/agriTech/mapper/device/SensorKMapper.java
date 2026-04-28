package com.example.agriTech.mapper.device;

import java.time.format.DateTimeFormatter;

import com.example.agriTech.dto.sensor.SensorDTOK;
import com.example.agriTech.model.Sensor;

public class SensorKMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static SensorDTOK toDTO(Sensor sensor) {
        if (sensor == null) return null;
        SensorDTOK dtok = new SensorDTOK();
        dtok.setSensorId(sensor.getSensorId());
        dtok.setSoilMoisture(sensor.getSoilMoisture());
        // Chuyển LocalDateTime từ MySQL sang String để gửi đi
        if (sensor.getRecordedAt() != null) {
            dtok.setRecordedAt(sensor.getRecordedAt().format(formatter));
        }
        return dtok;
    }

}
