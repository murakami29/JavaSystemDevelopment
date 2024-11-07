package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
