package com.example.agriTech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.agriTech.dto.auth.LoginResponseDTO;
import com.example.agriTech.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AuthController {
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Thêm hàm này để hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trả về /WEB-INF/view/login.jsp
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String pass,HttpSession session,Model model) {
        try {
            LoginResponseDTO loginResponseDTO = authService.login(username, pass);
            if(loginResponseDTO.getRole() != null && loginResponseDTO.getRole() != 0) {
                // Lưu thông tin vào session để duy trì đăng nhập
                session.setAttribute("Admin", loginResponseDTO);
                // Trả về trang chủ admin (index.jsp)
                return "redirect:/admin/dashboard";
            } else {
                model.addAttribute("error", "Bạn không có quyền truy cập trang quản trị!");
                return "login"; // Quay lại trang login.jsp
            }
        } catch (Exception e) {
           model.addAttribute("error", e.getMessage());
           return "login"; // quay lai trang login
        }

    }
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        if (session.getAttribute("Admin") == null) {
            return "redirect:/admin/login";
        }
        return "admin/dashboard"; // Trả về file /WEB-INF/jsp/admin/dashboard.jsp
    }
    // Thêm hàm đăng xuất cho chuyên nghiệp
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa sạch session
        return "redirect:/admin/login";
    }

    
}
