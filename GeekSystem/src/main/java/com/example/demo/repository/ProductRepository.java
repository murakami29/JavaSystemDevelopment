package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // キーワードによる検索クエリ
    @Query("SELECT p FROM Product p " +
           "JOIN FETCH p.smallCategory sc " +
           "JOIN FETCH p.manufacturer m " +
           "WHERE p.name LIKE %:keyword% " +
           "OR sc.name LIKE %:keyword% " +
           "OR m.name LIKE %:keyword% " +
           "OR p.details LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    // カテゴリIDとキーワードによる検索クエリ
    @Query("SELECT p FROM Product p " +
           "JOIN FETCH p.smallCategory sc " +
           "JOIN FETCH sc.middleCategory mc " +
           "JOIN FETCH mc.largeCategory lc " +
           "JOIN FETCH p.manufacturer m " +
           "WHERE (:largeCategoryId IS NULL OR lc.id = :largeCategoryId) " +  // largeCategoryIdが指定されている場合
           "AND (:middleCategoryId IS NULL OR mc.id = :middleCategoryId) " +  // middleCategoryIdが指定されている場合
           "AND (:smallCategoryId IS NULL OR sc.id = :smallCategoryId) " +  // smallCategoryIdが指定されている場合
           "AND (:keyword IS NULL OR p.name LIKE %:keyword% " +
           "OR sc.name LIKE %:keyword% " +
           "OR m.name LIKE %:keyword% " +
           "OR p.details LIKE %:keyword%)")  // キーワードによる部分一致検索
    List<Product> findByCategoriesAndKeyword(@Param("largeCategoryId") Long largeCategoryId,
                                             @Param("middleCategoryId") Long middleCategoryId,
                                             @Param("smallCategoryId") Long smallCategoryId,
                                             @Param("keyword") String keyword);
    
    List<Product> findBySmallCategoryId(Long smallId);

}
