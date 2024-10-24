package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
//	// 店舗の発注履歴情報を取得するメソッド
//    @Query("SELECT new com.example.demo.response.StoreOrderResponse("
//         + "s.name, "           // 
//         + "s.address, "           // 中カテゴリ名
//         + "sc.name, "           // 小カテゴリ名
//         + "p.id, p.name, p.details, "  // 商品ID、商品名、商品の説明
//         + "p.costPrice, "              // 仕入れ原価
//         + "p.manufacturerSuggestedRetailPrice) " // メーカ希望小売価格
//         + "FROM Store s "
//         + "JOIN s.smallCategory sc "
//         + "JOIN sc.middleCategory mc "
//         + "JOIN mc.largeCategory lc")
//    List<ProductCategoryResponse> findAllProductCategories();
}

