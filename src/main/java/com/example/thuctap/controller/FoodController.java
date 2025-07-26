package com.example.thuctap.controller;


import com.example.thuctap.models.Food;
import com.example.thuctap.service.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodRepository repo;


    @GetMapping({"", "/"})
    public String ShowProductList(Model model) {
        List<Food> foodList = repo.findAll();
        model.addAttribute("food", foodList); // trùng với th:each
        return "food/food"; // đảm bảo file đúng đường dẫn
    }
}
