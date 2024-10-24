package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.OrderHistory;
import com.example.demo.response.ProductCategoryResponse;
import com.example.demo.response.StoreOrderResponse;
import com.example.demo.service.OrderHistoryService;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
    private ProductService productService;
	
    @Autowired
    private OrderHistoryService orderHistoryService;
	
	// 商品とカテゴリの一覧を取得するエンドポイント
    @GetMapping("/products/categories")
    public ResponseEntity<List<ProductCategoryResponse>> getCategories() {
        List<ProductCategoryResponse> categories = productService.getProductCategories();
        return ResponseEntity.ok(categories);
    }

    // 店舗と発注履歴の一覧を取得するエンドポイント
    @GetMapping("/stores/orders")
    public ResponseEntity<List<StoreOrderResponse>> getOrders() {
        List<OrderHistory> orderHistories = orderHistoryService.findAllOrders(); // すべての発注履歴を取得
        List<StoreOrderResponse> orders = orderHistoryService.createStoreOrderResponseList(orderHistories);
        return ResponseEntity.ok(orders);
    }
}
