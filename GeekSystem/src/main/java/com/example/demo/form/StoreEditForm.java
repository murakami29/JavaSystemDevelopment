package com.example.demo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreEditForm {
    
    @NotNull
    private Long storeId; // 店舗ID

    @NotNull
    private String name; // 店舗名

    @NotNull
    private String address; // 店舗住所

}
