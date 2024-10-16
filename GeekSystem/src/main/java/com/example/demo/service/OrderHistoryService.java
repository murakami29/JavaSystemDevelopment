package com.example.demo.service;

import java.util.List;
//import java.util.List;
import java.util.Optional;

import com.example.demo.entity.OrderHistory;

public interface OrderHistoryService {

    // 特定の商品に基づく発注履歴を取得
	// List<OrderHistory> findByProductId(Long productId);
    Optional<OrderHistory> findLatestByProductId(Long productId);

    // 特定の店舗に基づく発注履歴を取得
    List<OrderHistory> findByStoreId(Long storeId);
    
    void save(OrderHistory orderHistory);
    
}