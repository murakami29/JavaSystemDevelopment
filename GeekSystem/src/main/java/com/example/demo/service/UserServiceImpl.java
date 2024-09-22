package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // User をデータベースから検索
        com.example.demo.entity.User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // ロール名を取得してコンソールに出力
        String permissionName = userEntity.getPermission().getName();
        System.out.println("Permission Name: " + permissionName); // パーミッション名をコンソールに出力
        String roleName;
        
        if ("管理者".equals(permissionName)) {
            roleName = "ADMIN";
        } else if ("一般".equals(permissionName)) {
            roleName = "USER";
        } else {
            throw new IllegalStateException("不明な権限名: " + permissionName);
        }
        
        System.out.println("Role Name: " + roleName); // 変換後のロール名をコンソールに出力
        
        // UserDetails のインスタンスを返す
        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(roleName) // ユーザーのロールを設定
                .build();
    }
    
    @Override
    public Optional<com.example.demo.entity.User> findByEmail(String email) {
        // メールでユーザーを検索し、Optionalで返す
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<com.example.demo.entity.User> findById(Long id) {
        // IDでユーザーを検索し、Optionalで返す
        return userRepository.findById(id);
    }
    
    @Override
    public void save(com.example.demo.entity.User user) {
    	
        userRepository.save(user);
    }
    
    @Override
    public List<com.example.demo.entity.User> findAll() {
        return userRepository.findAll();
    }
}
