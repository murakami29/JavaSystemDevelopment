package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Store;

public interface StoreService {
    List<Store> findAll();  // 全ての店舗を取得
    Optional<Store> findById(Long id);  // 店舗をIDで検索
    void save(Store store); // 店舗情報を保存
//    List<ProductCategoryResponse> getStoreOrders();
}
