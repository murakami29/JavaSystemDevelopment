package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderDemo {
    public static void main(String[] args) {
        // BCryptPasswordEncoder のインスタンスを作成
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // ハッシュ化したいパスワードを指定
        String rawPassword = "user_password";
        
        // パスワードをハッシュ化
        String encodedPassword = encoder.encode(rawPassword);
        
        // ハッシュ化されたパスワードを出力
        System.out.println("Encoded password: " + encodedPassword);
    }
}
