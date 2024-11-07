package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.form.LoginForm;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        String error = request.getParameter("error");
        if ("true".equals(error)) {
            model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません");
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @GetMapping("/home")
    public String showHome() {
        return "top";
    }
}
