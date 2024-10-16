package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderHistory;
import com.example.demo.repository.OrderHistoryRepository;

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
}
