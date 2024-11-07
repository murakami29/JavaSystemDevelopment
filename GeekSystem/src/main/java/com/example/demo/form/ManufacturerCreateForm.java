package com.example.demo.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManufacturerCreateForm {

    @NotNull
    private String name; // 名

}
