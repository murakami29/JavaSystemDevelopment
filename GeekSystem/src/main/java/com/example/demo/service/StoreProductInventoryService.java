package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.StoreProductInventory;

public interface StoreProductInventoryService {
    // 商品IDに基づいて在庫情報を取得するメソッド
    List<StoreProductInventory> findByProductId(Long productId);
 
    // 商品IDと店舗IDに基づいて在庫情報を取得するメソッド
    Optional<StoreProductInventory> findByProductAndStore(Long productId, Long storeId);
    
    // 在庫数を更新するメソッド
    void updateInventory(Long productId, Long storeId, int quantity);

}
