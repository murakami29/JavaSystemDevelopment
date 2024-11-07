package com.example.demo.service;

import java.util.List;
//import java.util.List;
import java.util.Optional;

import com.example.demo.entity.OrderHistory;
import com.example.demo.response.StoreOrderResponse;

public interface OrderHistoryService {

    // 特定の商品に基づく発注履歴を取得
	// List<OrderHistory> findByProductId(Long productId);
    Optional<OrderHistory> findLatestByProductId(Long productId);

    // 特定の店舗に基づく発注履歴を取得
    List<OrderHistory> findByStoreId(Long storeId);
    
    // すべての発注履歴を取得
    List<OrderHistory> findAllOrders();
    
    void save(OrderHistory orderHistory);
    
    OrderHistory findById(Long id); // OrderHistoryをIDで検索するメソッド
    StoreOrderResponse createStoreOrderResponse(OrderHistory orderHistory); // OrderHistoryからStoreOrderResponseを作成するメソッド
    
    List<StoreOrderResponse> createStoreOrderResponseList(List<OrderHistory> orderHistories);
}