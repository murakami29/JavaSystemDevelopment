package com.example.demo.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // 全フィールドを引数にとるコンストラクタを生成
@NoArgsConstructor    // 引数なしのコンストラクタも生成
public class StoreOrderResponse {
	private String storeName;
	private String storeAddress;
	
    // 商品一覧をリストで持つ
    private List<ProductInfo> products;
    
    // 商品発注履歴一覧をリストで持つ
    private List<OrderHistoryInfo> orderHistories;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private String productName;
        private BigDecimal storeProductPrice;
        private int orderHistoryInventoryAtOrderTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderHistoryInfo {
        private String orderHistoryProduct;
        private String orderHistoryUserName;
        private int orderHistoryQuantity;
        private BigDecimal orderHistoryTotalAmount;
    }
//	// 商品一覧
//	private String productName;
//	private BigDecimal storeProductPricePrice;
//	private int storeProductInventoryProductInventory;
//	// 商品発注履歴一覧
//	private String orderHistoryProduct;
//	private User orderHistoryUser;
//	private int orderHistoryQuantity;
//	private BigDecimal orderHistoryTotalAmount;
}
