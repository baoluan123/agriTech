package com.example.agriTech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Account;
import com.example.agriTech.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // Tìm thông tin chi tiết (User) dựa trên đối tượng Account
    // Spring sẽ tự hiểu và thực hiện JOIN hoặc tìm theo account_id
    Optional<User> findByAccount(Account account);

    // Nếu bạn muốn tìm nhanh bằng ID của Account mà không cần truyền nguyên Object
    Optional<User> findByAccountId(Long accountId);
}
