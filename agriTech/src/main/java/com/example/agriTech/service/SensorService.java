package com.example.agriTech.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.agriTech.dto.sensor.SensorDTO;
import com.example.agriTech.model.Device;
import com.example.agriTech.model.Notification;
import com.example.agriTech.model.PlantUser;
import com.example.agriTech.model.Sensor;
import com.example.agriTech.repository.DeviceRepository;
import com.example.agriTech.repository.NotificationRepository;
import com.example.agriTech.repository.PlantUserRepository;
import com.example.agriTech.repository.SensorRepository;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;
    private final PlantUserRepository plantUserRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationRepository notificationRepository;
    // Biến lưu thời điểm lưu Log cuối cùng (để giãn cách 5 phút) ngăn nước tràn dữ liệu
    private static LocalDateTime lastLogTime = LocalDateTime.MIN;
    public SensorService(SensorRepository sensorRepository,PlantUserRepository plantUserRepository,DeviceRepository deviceRepository,NotificationRepository notificationRepository) {
        this.sensorRepository = sensorRepository;
        this.plantUserRepository = plantUserRepository;
        this.deviceRepository = deviceRepository;
        this.notificationRepository = notificationRepository;
    }
    @Transactional
    public void processData(SensorDTO dto) {
        // 1. Tìm Thiết bị dựa trên deviceCode gửi từ ESP32
        Device device = this.deviceRepository.findByDeviceCode(dto.getSensorId());
        if(device == null){
            return;
        }
        // 2. Tìm Cây đang được gắn với thiết bị này
        PlantUser plantUser = this.plantUserRepository.findByDevice(device);
        // 3. CẬP NHẬT TRẠNG THÁI (Nếu có PlantUser)
        if(plantUser != null ) {
            //B1: luôn update để App Kotlin lấy được số mới nhất
            // Cập nhật lần cuối tưới nếu trạng thái là UOT
            //**** */
            if("UOT".equalsIgnoreCase(dto.getStatus())){
                //**** */
                plantUser.setLastWatered(LocalDateTime.now());
                this.plantUserRepository.save(plantUser);
            }
            checkAndAction(dto);
        }
        // 4. LƯU LỊCH SỬ (Giãn cách 5 phút để SQL không bị quá tải)
        if(LocalDateTime.now().isAfter(lastLogTime.plusMinutes(20))) {
            //mapping sau
            Sensor log = new Sensor();
            log.setDevice(device);
            log.setSensorId(dto.getSensorId());
            log.setTemperature(dto.getTemperature());
            log.setHumidity(dto.getHumidity());
            log.setSoilMoisture(dto.getSoilMoisture());
            log.setStatus(dto.getStatus());
            log.setRecordedAt(LocalDateTime.now());
            this.sensorRepository.save(log);
            lastLogTime = LocalDateTime.now(); // Cập nhật mốc thời gian đã lưu
            System.out.println(">>> Đã lưu Log cho thiết bị: " + dto.getSensorId());
        }                        
    }

    private void checkAndAction(SensorDTO dto) {
        // 1. Gọt dữ liệu thô
    if (dto.getSensorId() == null || dto.getSensorId().isEmpty()) return;
        PlantUser plantUser = this.plantUserRepository.findByDeviceDeviceCode(dto.getSensorId())
                                .orElse(null);
        if(plantUser != null) {
            // Device device = plantUser.getDevice(); // Lấy device từ quan hệ
            
            // 2. Logic theo Tần suất (Thời gian) text{Interval Hours} = waterFrequency (số ngày) x 24 giờ
            Integer daysBetween = plantUser.getPlant().getWaterFrequency();
            Double intervalHours = daysBetween * 24.0;
            // 2. Tính thời gian đã trôi qua
            boolean isTimeToGo = false;
            if(plantUser.getLastWatered() != null ) {
                // Tính số GIỜ đã trôi qua kể từ lần tưới cuối
                long hoursSinceLastWater = java.time.Duration.between(plantUser.getLastWatered(), LocalDateTime.now()).toHours();
                if(hoursSinceLastWater >= intervalHours) {
                    isTimeToGo = true;
                }

            } else {
                isTimeToGo = true; // Chưa tưới bao giờ thì mặc định là đến lịch
            }

            //logic theo do am
            Double humidity = (plantUser.getPlant().getIdealHumidity()) * 0.8;
            Double currentMoisturePercent = ((4095.0 - dto.getSoilMoisture())/4095.0) * 100; // lam tron chu so sau
            boolean isTooDry = currentMoisturePercent < humidity;
            if(isTooDry) {
                String title = "Cảnh báo thiếu nước";
                //* */
                boolean isNotified = notificationRepository.existsByDeviceIdAndTitleAndCreatedAtAfter(
                plantUser.getDevice().getId(), title, LocalDateTime.now().minusMinutes(30));
                if(!isNotified) {
                    //* */
                    saveNotification(plantUser, title, "Đất tại '" + plantUser.getCustomName() + "' đang khô (" + currentMoisturePercent + "%). Hãy tưới cây!");
                }
                // System.out.println(">>> [10s CHECK] Cảnh báo thiếu nước tại thiết bị: " + dto.getSensorId());
                // Gửi lệnh tưới hoặc thông báo ở đây
            } else if(isTimeToGo) {
                String title = "Lịch tưới định kỳ";
                //* */
                boolean isNotified = notificationRepository.existsByDeviceIdAndTitleAndCreatedAtAfter(
                    plantUser.getDevice().getId(), title, LocalDateTime.now().minusHours(12)); // Lịch thì 12h báo 1 lần thôi
                    if (!isNotified) {
                        //*/ */
                saveNotification(plantUser, title, 
                    "Đã đến lúc tưới nước định kỳ cho '" + plantUser.getCustomName() + "' theo lịch.");
            }
                System.out.println(" CẢNH BÁO: Đã đến lịch tưới định kỳ (mặc dù đất vẫn ẩm)." + dto.getSensorId());
               
            }
        }
    }
    //DTO sau
    private void saveNotification(PlantUser plantUser, String title, String message) {
        Notification notify = new Notification();
        notify.setDevice(plantUser.getDevice());
        notify.setPlantUser(plantUser);
        notify.setUser(plantUser.getUser());
        notify.setTitle(title);
        notify.setMessage(message);
        notify.setIsRead(false);
        notify.setCreatedAt(LocalDateTime.now());
        this.notificationRepository.save(notify);
        // Bước này là để ông debug xem nó đã lấy đúng User chưa
    System.out.println(">>> [NOTIFY SAVED] " + title + " cho User ID: " + plantUser.getUser().getId());
    }

}
