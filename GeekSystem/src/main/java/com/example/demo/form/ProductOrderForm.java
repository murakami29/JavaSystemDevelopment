package com.example.demo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductOrderForm {
    @NotNull
    private Long productId; // 商品ID

    @NotNull
    private Long storeId; // 店舗ID

    @NotNull
    private int quantity; // 発注数

    // 追加: ユーザーIDを追加
    private Long userId; // ユーザーID
}
