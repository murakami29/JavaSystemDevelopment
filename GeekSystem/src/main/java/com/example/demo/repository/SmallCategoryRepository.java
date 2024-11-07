package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.SmallCategory;

public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Long>{
	// 中カテゴリIDに基づいて小カテゴリを取得するクエリ
    List<SmallCategory> findByMiddleCategoryId(Long middleCategoryId);

    // MiddleCategoryに基づいて小カテゴリを取得するメソッド
    List<SmallCategory> findByMiddleCategory(MiddleCategory middleCategory);
    
    // 新しいメソッド：大カテゴリIDによる小カテゴリの検索
    @Query("SELECT sc FROM SmallCategory sc WHERE sc.middleCategory.largeCategory.id = :largeCategoryId")
    List<SmallCategory> findByLargeCategoryId(@Param("largeCategoryId") Long largeCategoryId);
    
}
