package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderHistory;
import com.example.demo.repository.OrderHistoryRepository;
import com.example.demo.response.StoreOrderResponse;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    
	@Autowired
    private OrderHistoryRepository orderHistoryRepository;
	
	@Override
    public Optional<OrderHistory> findLatestByProductId(Long productId) {
        return orderHistoryRepository.findFirstByProductIdOrderByOrderDateDesc(productId);
    }
	
	@Override
	public List<OrderHistory> findByStoreId(Long storeId) {
	    return orderHistoryRepository.findByStoreId(storeId); // リポジトリにこのメソッドが必要です
	}

    @Override
    public void save(OrderHistory orderHistory) {
        orderHistoryRepository.save(orderHistory);
    }
    
    @Override
    public OrderHistory findById(Long id) {
        return orderHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderHistory not found")); // エラーハンドリングは必要に応じてカスタマイズ
    }
    
    @Override
    public List<OrderHistory> findAllOrders() {
        return orderHistoryRepository.findAll(); // すべての発注履歴を取得
    }
    
    @Override
    public StoreOrderResponse createStoreOrderResponse(OrderHistory orderHistory) {
        StoreOrderResponse response = new StoreOrderResponse();

        // 店舗情報の設定
        response.setStoreName(orderHistory.getStore().getName());
        response.setStoreAddress(orderHistory.getStore().getAddress());

        // 商品情報のリストを作成
        List<StoreOrderResponse.ProductInfo> productInfoList = new ArrayList<>();
        StoreOrderResponse.ProductInfo productInfo = createProductInfo(orderHistory);
        productInfoList.add(productInfo);
        response.setProducts(productInfoList);

        // 発注履歴情報のリストを作成
        List<StoreOrderResponse.OrderHistoryInfo> orderHistoryInfoList = new ArrayList<>();
        StoreOrderResponse.OrderHistoryInfo orderHistoryInfo = createOrderHistoryInfo(orderHistory);
        orderHistoryInfoList.add(orderHistoryInfo);
        response.setOrderHistories(orderHistoryInfoList);

        return response;
    }
    
 // 複数のOrderHistoryからStoreOrderResponseのリストを作成
    @Override
    public List<StoreOrderResponse> createStoreOrderResponseList(List<OrderHistory> orderHistories) {
        Map<Long, StoreOrderResponse> responseMap = new HashMap<>();

        for (OrderHistory orderHistory : orderHistories) {
            Long storeId = orderHistory.getStore().getId();
            StoreOrderResponse response = responseMap.computeIfAbsent(storeId, id -> {
                StoreOrderResponse newResponse = new StoreOrderResponse();
                newResponse.setStoreName(orderHistory.getStore().getName());
                newResponse.setStoreAddress(orderHistory.getStore().getAddress());
                newResponse.setProducts(new ArrayList<>());
                newResponse.setOrderHistories(new ArrayList<>());
                return newResponse;
            });

            // 商品情報を追加
            addProductInfo(response, orderHistory);
            
            // 発注履歴情報を追加
            StoreOrderResponse.OrderHistoryInfo orderHistoryInfo = createOrderHistoryInfo(orderHistory);
            response.getOrderHistories().add(orderHistoryInfo);
        }

        return new ArrayList<>(responseMap.values());
    }
    private StoreOrderResponse.ProductInfo createProductInfo(OrderHistory orderHistory) {
        StoreOrderResponse.ProductInfo productInfo = new StoreOrderResponse.ProductInfo();
        productInfo.setProductName(orderHistory.getProduct().getName());
        productInfo.setStoreProductPrice(orderHistory.getProduct().getCostPrice());
        productInfo.setOrderHistoryInventoryAtOrderTime(orderHistory.getInventoryAtOrderTime());
        return productInfo;
    }

    private StoreOrderResponse.OrderHistoryInfo createOrderHistoryInfo(OrderHistory orderHistory) {
        StoreOrderResponse.OrderHistoryInfo orderHistoryInfo = new StoreOrderResponse.OrderHistoryInfo();
        orderHistoryInfo.setOrderHistoryProduct(orderHistory.getProduct().getName());
        orderHistoryInfo.setOrderHistoryUserName(orderHistory.getUser().getFirstName() + " " + orderHistory.getUser().getLastName());
        orderHistoryInfo.setOrderHistoryQuantity(orderHistory.getQuantity());
        orderHistoryInfo.setOrderHistoryTotalAmount(orderHistory.getTotalAmount());
        return orderHistoryInfo;
    }

    private void addProductInfo(StoreOrderResponse response, OrderHistory orderHistory) {
        String productName = orderHistory.getProduct().getName();
        
        // すでに同じ商品が追加されているかチェック
        boolean productExists = response.getProducts().stream()
            .anyMatch(p -> p.getProductName().equals(productName));
        
        if (!productExists) {
            StoreOrderResponse.ProductInfo productInfo = createProductInfo(orderHistory);
            response.getProducts().add(productInfo);
        }
    }
}
