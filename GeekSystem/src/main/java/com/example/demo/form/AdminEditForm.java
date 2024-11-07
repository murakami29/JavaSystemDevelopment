package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminEditForm {

    @NotNull
    private Long id; // ユーザーID

    @NotNull
    private Long storeId; // 店舗ID

    @NotNull
    private Long roleId; // 役職ID

    @NotNull
    private Long permissionId; // 権限ID

    @NotNull
    private String firstName; // 名

    @NotNull
    private String lastName; // 姓

    @Email
    @NotNull
    private String email; // メールアドレス

    @NotNull
    private String phone; // 電話番号
    
}
