package com.example.agriTech.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long accountId;
    private String username;
    private String fullName;
    private Integer role;
    private String message;

    
    private String phone;    // Thêm cái này
    private String address;  // Thêm cái này
    
}
