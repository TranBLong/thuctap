package com.example.thuctap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login/login"; // maps to: templates/login/login.html
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "login/signup"; // maps to: templates/login/signup.html
    }
}
