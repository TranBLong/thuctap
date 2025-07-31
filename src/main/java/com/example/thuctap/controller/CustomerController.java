package com.example.thuctap.controller;

import com.example.thuctap.models.Customer;
import com.example.thuctap.models.CustomerLoginRequest;
import com.example.thuctap.models.CustomerRegisterRequest;
import com.example.thuctap.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // API đăng ký người dùng
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody CustomerRegisterRequest request) {
        Customer saved = customerService.register(request);
        return ResponseEntity.ok(saved);
    }

    // API đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerLoginRequest request) {
        Customer customer = customerService.login(request);
        if (customer == null) {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
        }
        return ResponseEntity.ok(customer);
    }

    // (Tùy chọn) API trả về dữ liệu JSON profile
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable int id) {
        Customer customer = customerService.getCustomerById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }
}
