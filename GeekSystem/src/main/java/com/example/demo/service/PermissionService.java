package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Permission;

public interface PermissionService {
    List<Permission> findAll();  // 全ての権限を取得
    Optional<Permission> findById(Long id);  // 権限をIDで検索
}
