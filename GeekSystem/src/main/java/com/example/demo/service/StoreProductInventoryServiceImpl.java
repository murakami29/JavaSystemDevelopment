package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.StoreProductInventory;
import com.example.demo.entity.StoreProductInventoryId;
import com.example.demo.repository.StoreProductInventoryRepository;

@Service
public class StoreProductInventoryServiceImpl implements StoreProductInventoryService {

    private final StoreProductInventoryRepository storeProductInventoryRepository;

    // コンストラクタでリポジトリをインジェクション
    @Autowired
    public StoreProductInventoryServiceImpl(StoreProductInventoryRepository storeProductInventoryRepository) {
        this.storeProductInventoryRepository = storeProductInventoryRepository;
    }

    // 商品IDに基づいて在庫情報を取得するメソッドを実装
    @Override
    public List<StoreProductInventory> findByProductId(Long productId) {
        return storeProductInventoryRepository.findByProductId(productId);
    }
    
 // 商品IDと店舗IDに基づいて在庫情報を取得するメソッドを実装
    @Override
    public Optional<StoreProductInventory> findByProductAndStore(Long productId, Long storeId) {
        return storeProductInventoryRepository.findByProductIdAndStoreId(productId, storeId);
    }

    // 在庫数を更新するメソッドを実装
    @Override
    public void updateInventory(Long productId, Long storeId, int quantity) {
        // 商品IDと店舗IDに基づいて在庫情報を取得
    	Optional<StoreProductInventory> inventoryOptional = storeProductInventoryRepository.findByProductIdAndStoreId(productId, storeId);

        if (inventoryOptional.isPresent()) {
            // 在庫数に注文数を加算して更新
        	StoreProductInventory inventory = inventoryOptional.get();
            inventory.setProductInventory(inventory.getProductInventory() + quantity);
            storeProductInventoryRepository.save(inventory);  // 在庫情報を更新
        } else {
            // 該当する在庫情報が存在しない場合の処理（新規作成など）
            StoreProductInventory newInventory = new StoreProductInventory();
            StoreProductInventoryId newInventoryId = new StoreProductInventoryId();
            
            newInventoryId.setProductId(productId);
            newInventoryId.setStoreId(storeId);
            newInventory.setId(newInventoryId);        // IDを設定
            newInventory.setProductInventory(quantity);
            storeProductInventoryRepository.save(newInventory);  // 新しい在庫情報を保存
        }
    }
}
