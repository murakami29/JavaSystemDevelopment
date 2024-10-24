package com.example.demo.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // 全フィールドを引数にとるコンストラクタを生成
@NoArgsConstructor    // 引数なしのコンストラクタも生成
public class ProductCategoryResponse {
    private String largeCategoryName;       // 大カテゴリ名
    private String middleCategoryName;      // 中カテゴリ名
    private String smallCategoryName;       // 小カテゴリ名
    private String productName;              // 商品名
    private String productDetails;       // 商品の説明
    private BigDecimal costPrice;                 // 仕入れ原価
    private BigDecimal manufacturerSuggestedRetailPrice; // メーカ希望小売価格
}
