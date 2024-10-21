package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.LargeCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.SmallCategory;

public interface CategoryService {

    List<LargeCategory> findAllLargeCategories();

    List<MiddleCategory> findAllMiddleCategories();

    List<SmallCategory> findAllSmallCategories();
    
    List<MiddleCategory> findMiddleCategoriesByLargeCategoryId(Long largeCategoryId);

    List<SmallCategory> findSmallCategoriesByMiddleCategoryId(Long middleCategoryId);
    
    List<SmallCategory> findSmallCategoriesByLargeCategoryId(Long largeCategoryId);
    
    List<SmallCategory> getSmallCategoriesByMiddleCategory(MiddleCategory middleCategory);

    LargeCategory findLargeCategoryBySmallCategory(SmallCategory smallCategory);
    
    Optional<LargeCategory> findLargeCategoryById(Long largeId);
    
    Optional<MiddleCategory> findMiddleCategoryById(Long middleId);
    
    Optional<SmallCategory> findSmallCategoryById(Long smallId);
    
}
