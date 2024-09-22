package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.User;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email); // メールでユーザーを検索
    
    // UserDetailsServiceのloadUserByUsernameメソッドも継承しているため、こちらも定義します
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    
    Optional<User> findById(Long id);
    
    void save(User user); // ユーザー情報を保存
    
    List<User> findAll();  // 全てのユーザー情報を取得
}
