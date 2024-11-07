package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Role;

public interface RoleService {
    List<Role> findAll();  // 全ての役職を取得
    Optional<Role> findById(Long id);  // 役職をIDで検索
}
