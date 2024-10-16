package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderHistory;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long>{
	// 最新の発注履歴を取得するクエリ
	//    @Query("SELECT o FROM OrderHistory o WHERE o.product.id = :productId ORDER BY o.orderDate DESC ")
	//    Optional<OrderHistory> findLatestByProductId(@Param("productId") Long productId);

	// 最新の発注履歴を取得するクエリメソッド
    Optional<OrderHistory> findFirstByProductIdOrderByOrderDateDesc(Long productId);
    
    // 店舗IDによる発注履歴の取得
    List<OrderHistory> findByStoreId(Long storeId);
    
}