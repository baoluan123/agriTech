package com.example.agriTech.api;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.dto.auth.LoginRequestDTO;
import com.example.agriTech.dto.auth.LoginResponseDTO;
import com.example.agriTech.dto.auth.RegisterRequestDTO;
import com.example.agriTech.service.AuthService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;
    public AuthApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        System.out.println("DTO nhận được: " + dto);
        try{
            LoginResponseDTO resurt = this.authService.login(dto.getUsername(),dto.getPassword());
            return ResponseEntity.ok(resurt);
        }catch(RuntimeException e) {
            // Trả về lỗi 400 kèm tin nhắn nếu sai pass/user
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {
        try{
            LoginResponseDTO resurt = this.authService.register(dto);
            return ResponseEntity.ok(resurt);
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}
