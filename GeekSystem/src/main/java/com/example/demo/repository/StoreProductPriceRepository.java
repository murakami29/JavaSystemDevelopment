package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.StoreProductPrice;
import com.example.demo.entity.StoreProductPriceId;

@Repository
public interface StoreProductPriceRepository extends JpaRepository<StoreProductPrice, StoreProductPriceId> {
    // 特別なクエリは不要なため、デフォルトのfindById(StoreProductPriceId id)メソッドが利用できます
}
