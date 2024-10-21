package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public List<Product> getProductsBySmallCategoryId(Long smallId) {
        return productRepository.findBySmallCategoryId(smallId);
    }
    
    @Override
    public List<Product> searchProductsByKeyword(String keyword) {
        if (StringUtils.hasText(keyword)) {
            // 部分一致検索をリポジトリで実行
            return productRepository.searchByKeyword(keyword);
        }
        return productRepository.findAll(); // キーワードがない場合は全件取得
    }
    
 // カテゴリIDとキーワードで商品を検索するメソッドを追加
    @Override
    public List<Product> searchProducts(Long largeCategoryId, Long middleCategoryId, Long smallCategoryId, String keyword) {
        // リポジトリにカテゴリIDとキーワードを渡して検索
        return productRepository.findByCategoriesAndKeyword(largeCategoryId, middleCategoryId, smallCategoryId, keyword);
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        // IDでユーザーを検索し、Optionalで返す
        return productRepository.findById(id);
    }
}
