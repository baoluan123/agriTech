package com.example.agriTech.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDTO {
     // Thông tin Account
    private String username;
    private String password;
    private Integer role;
    private String fullName;

    // Thông tin User (Dùng khi role = "1")
    private String address;
    private String phone;

    // Thông tin Admin (Nếu cần truyền thêm gì đó lúc đăng ký)
    private Integer levelAdmin;
    
}
