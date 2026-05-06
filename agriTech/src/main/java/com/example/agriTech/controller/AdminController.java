package com.example.agriTech.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.agriTech.dto.plant.PlantListDTO;
import com.example.agriTech.dto.plantuser.PlantUserDTO;
import com.example.agriTech.model.Device;
import com.example.agriTech.model.Plant;
import com.example.agriTech.model.User;
import com.example.agriTech.service.DeviceService;
import com.example.agriTech.service.PlantService;
import com.example.agriTech.service.PlantUserService;
import com.example.agriTech.service.UserService;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PlantUserService plantUserService;
    private final DeviceService deviceService;
    private final PlantService plantService;
    public AdminController(UserService userService,PlantUserService plantUserService,DeviceService deviceService,PlantService plantService) {
        this.plantService = plantService;
        this.userService = userService;
        this.plantUserService= plantUserService;
        this.deviceService = deviceService;
    }
    @GetMapping("/users")
    public String showUser(HttpSession session,Model model) {
        if(session.getAttribute("Admin") == null) {
            return "redirect:/admin/login";
        }
        // 2. Lấy dữ liệu
        List<User> list = this.userService.getAllUser();
        // 3. Đẩy dữ liệu ra view
        model.addAttribute("userList", list);
        // Trả về file: /WEB-INF/view/admin/user-management.jsp
        return "admin/users/user";
    }

    @GetMapping("/gardens/{id}") 
    public String showMyPlant(@PathVariable("id") Long userId,HttpSession session,Model model) {
        if(session.getAttribute("Admin")==null) {
            return "redirect:/admin/login";
        }
        List<PlantUserDTO> plantUsers = this.plantUserService.getMyPlantList(userId);

        model.addAttribute("plants", plantUsers);
        model.addAttribute("userId", userId);
        return "admin/users/my-plants";


    }
    @GetMapping("/devices")
    public String showDevice(HttpSession session,Model model) {
        if(session.getAttribute("Admin") == null) {
            return "redirect:/admin/login";
        }
        List<Device> devices = this.deviceService.getAllDevice();
        model.addAttribute("listDevice", devices);
        return "admin/device/device";
    }

    @GetMapping("/plants")
    public String showPlant(HttpSession session,Model model) {
        if(session.getAttribute("Admin") == null) {
            return "redirect:/admin/login";
        }
        List<PlantListDTO> pList = this.plantService.getAllPlant();
        model.addAttribute("plantListDTO", pList);
        return "admin/plant/plantList";
    }
    @GetMapping("/plants/add")
    public String showAddPlant(Model model) {
        model.addAttribute("plant", new Plant());
        model.addAttribute("pageTitle", "Thêm giống cây mới");
        return "admin/plant/plant-form"; // Tạo thêm file plant-form.jsp
    }
    @GetMapping("/plants/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Plant plant = this.plantService.getPlantById(id);
        model.addAttribute("plant", plant);
        model.addAttribute("pageTitle", "Chỉnh sửa giống cây");
        return "admin/plant/plant-form";
    }
    // 4. Xử lý Lưu (Cả Thêm và Sửa)
    @PostMapping("/plants/save")
    public String savePlant(@ModelAttribute("plant") Plant plant,RedirectAttributes ra) {
        try {
           this.plantService.savePlant(plant);
           ra.addFlashAttribute("message", "Đã lưu thông tin cây trồng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
         return "redirect:/admin/dashboard";
    }
    // 5. Xóa
    @GetMapping("/plants/delete/{id}")
    public String deletePlant(@PathVariable("id") Long id, RedirectAttributes ra) {
        this.plantService.deletePlant(id);
        ra.addFlashAttribute("message", "Đã xóa cây trồng!");
        return "redirect:/admin/dashboard";
    }

}
