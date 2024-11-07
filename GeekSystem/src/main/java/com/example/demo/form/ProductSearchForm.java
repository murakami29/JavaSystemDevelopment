package com.example.demo.form;

import lombok.Data;

@Data
public class ProductSearchForm {
    private Long largeCategoryId;
    private Long middleCategoryId;
    private Long smallCategoryId;
    private String searchText;
}