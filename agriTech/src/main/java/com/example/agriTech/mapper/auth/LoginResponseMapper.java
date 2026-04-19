package com.example.agriTech.mapper.auth;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.auth.LoginResponseDTO;
import com.example.agriTech.model.Account;
import com.example.agriTech.model.User;

@Component
public class LoginResponseMapper {

    public LoginResponseDTO toLoginResponseDTO(Account account,User user) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setAccountId(account.getId());
        dto.setUsername(account.getUsername());
        dto.setFullName(account.getFullName());
        dto.setRole(account.getRole());
        if(user != null) {
            dto.setAddress(user.getAddress());
            dto.setPhone(user.getPhone());
        }
        return dto;
    }
    
}
