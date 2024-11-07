package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Manufacturer;

public interface ManufacturerService {
	
	List<Manufacturer> findAll();
	
	Optional<Manufacturer> findById(Long id);
	
    void save(Manufacturer manufacturer); // ユーザー情報を保存
    // 管理者を削除
    void deleteById(Long id);

}
