package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Product;
import com.example.demo.response.ProductCategoryResponse;

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
    
 // 商品とカテゴリ情報を取得するためのメソッド
    @Query("SELECT new com.example.demo.response.ProductCategoryResponse("
         + "lc.name, "           // 大カテゴリ名
         + "mc.name, "           // 中カテゴリ名
         + "sc.name, "           // 小カテゴリ名
         + "p.name, p.details, "  // 商品ID、商品名、商品の説明
         + "p.costPrice, "              // 仕入れ原価
         + "p.manufacturerSuggestedRetailPrice) " // メーカ希望小売価格
         + "FROM Product p "
         + "JOIN p.smallCategory sc "
         + "JOIN sc.middleCategory mc "
         + "JOIN mc.largeCategory lc")
    List<ProductCategoryResponse> findAllProductCategories();
}
