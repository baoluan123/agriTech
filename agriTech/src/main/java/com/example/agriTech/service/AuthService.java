package com.example.agriTech.service;




import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.agriTech.dto.auth.LoginResponseDTO;
import com.example.agriTech.dto.auth.RegisterRequestDTO;
import com.example.agriTech.mapper.auth.LoginResponseMapper;
import com.example.agriTech.mapper.auth.RegisterResponseMapper;
import com.example.agriTech.model.Account;
import com.example.agriTech.model.Admin;
import com.example.agriTech.model.User;
import com.example.agriTech.repository.AccountRepository;
import com.example.agriTech.repository.AdminRepository;
import com.example.agriTech.repository.UserRepository;
@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RegisterResponseMapper registerResponseMapper;
    private final LoginResponseMapper loginResponseMapper;
    public AuthService(AccountRepository accountRepository,AdminRepository adminRepository,UserRepository userRepository,RegisterResponseMapper registerResponseMapper,LoginResponseMapper loginResponseMapper) {
        this.accountRepository = accountRepository;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.registerResponseMapper = registerResponseMapper;
        this.loginResponseMapper = loginResponseMapper;
    }
    @Transactional // Thêm cái này để nếu lưu User lỗi thì Account cũng không được tạo (an toàn data)
    public LoginResponseDTO register(RegisterRequestDTO dto) {
        if(this.accountRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Tài khoản [" + dto.getUsername() + "] đã tồn tại!");
        }
        // 2. Lưu Account
        Account acc = this.registerResponseMapper.toAccountEntity(dto);
        Account accSave = this.accountRepository.save(acc);
// 3. Lưu chi tiết User hoặc Admin
        if(dto.getRole() == 1) {
            Admin a = this.registerResponseMapper.toAdminEntity(dto, accSave);
            this.adminRepository.save(a);
        } else {
            User u = this.registerResponseMapper.toUserEntity(dto, accSave);
            this.userRepository.save(u);
        }
        // 4. Dùng Mapper chuyển Account vừa save thành LoginResponseDTO để trả về
        User u1 = null;
        if(accSave.getRole() != null && accSave.getRole() != 1) {
            u1 = this.userRepository.findByAccount(accSave).orElse(null);
        }
        LoginResponseDTO registerdDto = this.loginResponseMapper.toLoginResponseDTO(accSave, u1);
        registerdDto.setMessage("Đăng ký và đăng nhập thành công!");
        return registerdDto;
    }
    public LoginResponseDTO login(String username, String password) {
        System.out.println("Đang tìm tài khoản: [" + username + "]"); // Kiểm tra xem username truyền vào có đúng không
        // 1. Tìm Account theo username
        Account a = this.accountRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("Tài khoản không tồn tại!"));
        // 2. Kiểm tra mật khẩu (Tạm thời so sánh chuỗi, sau này dùng BCrypt)        
        if(!a.getPassword().equals(password)) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
// 3. Nếu là USER (role = 0), tìm thông tin Profile trong bảng User
        User u = null;
        if(a.getRole() != null && a.getRole()==0) {
            u = this.userRepository.findByAccount(a).orElse(null);
        }

        LoginResponseDTO responseDTO = this.loginResponseMapper.toLoginResponseDTO(a, u);
        responseDTO.setMessage("Đăng nhập thành công!");
        return responseDTO;
    }
}
