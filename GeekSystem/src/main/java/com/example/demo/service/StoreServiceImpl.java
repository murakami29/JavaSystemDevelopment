package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Store;
import com.example.demo.repository.StoreRepository;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }
    
    @Override
    public void save(com.example.demo.entity.Store store) {
        storeRepository.save(store);
    }
    
    // 店舗の発注履歴情報を取得するメソッド
//    @Override
//    public List<ProductCategoryResponse> getStoreOrders() {
//        // リポジトリからカテゴリ情報を取得するロジックを追加します
//        return storeRepository.findAllStoreOrders(); // 仮メソッド名、実際には実装する必要があります
//    }
}
