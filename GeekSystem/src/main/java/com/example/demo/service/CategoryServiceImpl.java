package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.LargeCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.SmallCategory;
import com.example.demo.repository.LargeCategoryRepository;
import com.example.demo.repository.MiddleCategoryRepository;
import com.example.demo.repository.SmallCategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final LargeCategoryRepository largeCategoryRepository;
    private final MiddleCategoryRepository middleCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;

    // コンストラクタインジェクション
    public CategoryServiceImpl(LargeCategoryRepository largeCategoryRepository,
                               MiddleCategoryRepository middleCategoryRepository,
                               SmallCategoryRepository smallCategoryRepository) {
        this.largeCategoryRepository = largeCategoryRepository;
        this.middleCategoryRepository = middleCategoryRepository;
        this.smallCategoryRepository = smallCategoryRepository;
    }
    
    @Override
    public List<LargeCategory> findAllLargeCategories() {
        return largeCategoryRepository.findAll();
    }

    @Override
    public List<MiddleCategory> findAllMiddleCategories() {
        return middleCategoryRepository.findAll();
    }

    @Override
    public List<SmallCategory> findAllSmallCategories() {
        return smallCategoryRepository.findAll();
    }
    
    @Override
    public List<MiddleCategory> findMiddleCategoriesByLargeCategoryId(Long largeCategoryId) {
        return middleCategoryRepository.findByLargeCategoryId(largeCategoryId);
    }
    
    @Override
    public List<SmallCategory> findSmallCategoriesByMiddleCategoryId(Long middleCategoryId) {
        return smallCategoryRepository.findByMiddleCategoryId(middleCategoryId);
    }
    
    @Override
    public List<SmallCategory> findSmallCategoriesByLargeCategoryId(Long largeCategoryId) {
        // 大カテゴリに関連する中カテゴリの小カテゴリを取得するロジックを実装
        return smallCategoryRepository.findByLargeCategoryId(largeCategoryId);
    }
    
    @Override
    public List<SmallCategory> getSmallCategoriesByMiddleCategory(MiddleCategory middleCategory) {
        // リポジトリを使って小カテゴリを取得する実装
        return smallCategoryRepository.findByMiddleCategory(middleCategory);
    }
    
    @Override
    public LargeCategory findLargeCategoryBySmallCategory(SmallCategory smallCategory) {
        // MiddleCategoryからLargeCategoryを取得
        if (smallCategory.getMiddleCategory() != null) {
            return smallCategory.getMiddleCategory().getLargeCategory();
        }
        return null; // MiddleCategoryが存在しない場合
    }
    
    @Override
    public Optional<LargeCategory> findLargeCategoryById(Long largeId) {
        return largeCategoryRepository.findById(largeId);	
    }
    
    @Override
    public Optional<MiddleCategory> findMiddleCategoryById(Long middleId) {
        return middleCategoryRepository.findById(middleId);
    }
    
    @Override
    public Optional<SmallCategory> findSmallCategoryById(Long smallId) {
        return smallCategoryRepository.findById(smallId);
    }
}
