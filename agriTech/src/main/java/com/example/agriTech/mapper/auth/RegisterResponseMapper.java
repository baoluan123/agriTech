package com.example.agriTech.mapper.auth;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.auth.RegisterRequestDTO;
import com.example.agriTech.model.Account;
import com.example.agriTech.model.Admin;
import com.example.agriTech.model.User;

@Component
public class RegisterResponseMapper {
    public Account toAccountEntity(RegisterRequestDTO dto) {
        Account acc = new Account();
        acc.setUsername(dto.getUsername());
        acc.setFullName(dto.getFullName());
        acc.setPassword(dto.getPassword());
        acc.setRole(dto.getRole());
        return acc;
    }
    public User toUserEntity(RegisterRequestDTO dto,Account account) {
        User u = new User();
        u.setAccount(account);
        u.setAddress(dto.getAddress());
        u.setPhone(dto.getPhone());
        return u;
    }
    public Admin toAdminEntity(RegisterRequestDTO dto,Account account) {
        Admin a = new Admin();
        a.setAccount(account);
        a.setLevelAdmin(dto.getLevelAdmin());
        return a;
    }
}
