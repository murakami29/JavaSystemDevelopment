package com.example.demo.form;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
