package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.StoreProductInventory;
import com.example.demo.entity.StoreProductInventoryId;

public interface StoreProductInventoryRepository extends JpaRepository<StoreProductInventory, StoreProductInventoryId>{
	List<StoreProductInventory> findByProductId(Long productId);
	Optional<StoreProductInventory> findByProductIdAndStoreId(Long productId, Long storeId);
	
}
