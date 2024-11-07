package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.StoreProductPrice;

public interface StoreProductPriceService {
	
	Optional<StoreProductPrice> findByProductIdAndStoreId(Long productId, Long storeId);
	
}
