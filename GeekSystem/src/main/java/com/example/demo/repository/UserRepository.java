package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User; // Userエンティティクラスの名前がUserの場合

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
