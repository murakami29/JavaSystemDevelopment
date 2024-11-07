package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.StoreProductPrice;
import com.example.demo.entity.StoreProductPriceId;
import com.example.demo.repository.StoreProductPriceRepository;

@Service
public class StoreProductPriceServiceImpl implements StoreProductPriceService {
    
	@Autowired
    private StoreProductPriceRepository storeProductPriceRepository;

	@Override
    public Optional<StoreProductPrice> findByProductIdAndStoreId(Long productId, Long storeId) {
	    // StoreProductPriceIdを作成
	    StoreProductPriceId id = new StoreProductPriceId();
	    id.setProductId(productId);
	    id.setStoreId(storeId);
	    
        // 複合キーで価格情報を検索し、Optionalで返す
	    return storeProductPriceRepository.findById(id);
	}
//	    Optional<StoreProductPrice> storeProductPriceOptional = storeProductPriceRepository.findById(id);
    	// 価格情報が見つからなかった場合は例外をスロー
//    	return storeProductPriceOptional.orElseThrow(() -> new RuntimeException("価格情報が見つかりませんでした: productId=" + productId + ", storeId=" + storeId));
	    // 例外をスローする既存メソッド
	    public StoreProductPrice findByProductIdAndStoreIdOrThrow(Long productId, Long storeId) {
	        return findByProductIdAndStoreId(productId, storeId)
	            .orElseThrow(() -> new RuntimeException("価格情報が見つかりませんでした: productId=" + productId + ", storeId=" + storeId));
    }
}
