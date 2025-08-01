package com.example.thuctap.controller;

import com.example.thuctap.models.Customer;
import com.example.thuctap.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String listUsers(Model model, @RequestParam(required = false) String keyword) {
        List<Customer> users;
        if (keyword != null && !keyword.isEmpty()) {
            users = customerRepository.findByFullNameContainingOrEmailContaining(keyword, keyword);
        } else {
            users = customerRepository.findAll();
        }
        model.addAttribute("users", users);
        return "customer/user-management";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new Customer());
        return "customer/create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") Customer user, Model model) {
        if (customerRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "❌ Email đã tồn tại!");
            return "customer/create-user";
        }

        customerRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        Customer user = customerRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "customer/edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") Customer user, Model model) {
        // Kiểm tra email đã tồn tại bởi người khác chưa
        Optional<Customer> existingUser = customerRepository.findByEmail(user.getEmail());

        if (!Integer.valueOf(user.getId()).equals(existingUser.get().getId())) {
            model.addAttribute("errorMessage", "❌ Email đã được người khác sử dụng!");
            model.addAttribute("user", user); // để giữ lại dữ liệu khi quay lại form
            return "customer/edit-user";
        }

        customerRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        customerRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
