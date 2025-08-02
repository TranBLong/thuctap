package com.example.thuctap.controller;

import com.example.thuctap.models.Customer;
import com.example.thuctap.models.CustomerLoginRequest;
import com.example.thuctap.models.CustomerRegisterRequest;
import com.example.thuctap.models.Menu;
import com.example.thuctap.service.CartService;
import com.example.thuctap.service.CustomerService;
import com.example.thuctap.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class CustomerViewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CartService cartService;

    // Hiển thị form đăng ký
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("customer", new CustomerRegisterRequest());
        return "customer/signup";
    }

    // Xử lý đăng ký người dùng
    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute("customer") CustomerRegisterRequest request) {
        customerService.register(request);
        return "redirect:/login";
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("customer", new CustomerLoginRequest());
        return "customer/login";
    }

    // Xử lý đăng nhập người dùng
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("customer") CustomerLoginRequest request, Model model) {
        Customer customer = customerService.login(request);
        if (customer == null) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng");
            return "customer/login";
        }
        return "redirect:/auth/customer/profile/" + customer.getId();
    }

    // Hiển thị trang profile người dùng
    @GetMapping("/customer/profile/{id}")
    public String viewProfile(@PathVariable Integer id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        List<Menu> menus = menuService.getAllMenus(); // hoặc getMenusByCustomerId(id) nếu menu cá nhân

        model.addAttribute("customer", customer);
        model.addAttribute("menus", menus);
        return "customer/profile"; // đường dẫn tới template
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int customerId,
                            @RequestParam int productId,
                            @RequestParam int quantity) {
        cartService.addItemToCart(customerId, productId, quantity);
        return "redirect:/cart/view/" + customerId;
    }

}
