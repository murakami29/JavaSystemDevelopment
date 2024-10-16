package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Product;


public interface ProductService {
	List<Product> findAll();
	List<Product> searchProductsByKeyword(String keyword);
	List<Product> searchProducts (Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, String keyword);
	Optional<Product> findById(Long id);
	Page<Product> findAll(Pageable pageable);
//	Page<Product> searchProductsByKeyword(String keyword, Pageable pageable);


}