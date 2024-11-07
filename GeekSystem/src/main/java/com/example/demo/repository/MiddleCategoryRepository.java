package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MiddleCategory;

@Repository
public interface MiddleCategoryRepository extends JpaRepository<MiddleCategory, Long>{
	
	// 大カテゴリIDに基づいて中カテゴリを取得するクエリ
    List<MiddleCategory> findByLargeCategoryId(Long largeCategoryId);
}
