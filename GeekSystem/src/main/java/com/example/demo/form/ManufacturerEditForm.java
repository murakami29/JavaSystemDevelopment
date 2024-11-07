package com.example.demo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManufacturerEditForm {

    @NotNull
    private Long id; // メーカID

    @NotNull
    private String name; // メーカ名
}
