package com.example.demo.service;

import com.example.demo.entity.StoreProductPrice;

public interface StoreProductPriceService {
	
	StoreProductPrice findByProductIdAndStoreId(Long productId, Long storeId);
}
