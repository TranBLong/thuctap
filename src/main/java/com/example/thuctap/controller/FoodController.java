package com.example.thuctap.controller;

import com.example.thuctap.models.Food;
import com.example.thuctap.repository.FoodRepository;
import com.example.thuctap.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodRepository repo;

    @Autowired
    private FoodService foodService;


    //1. Trả về danh sách thức ăn để hiển thị trên view
    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Food> foodsList = repo.findAll();
        model.addAttribute("foods", foodsList);
        return "food/food";
    }

    //2. API lấy chi tiết 1 món ăn theo ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Food> getFoodById(@PathVariable("id") Integer foodId) {
        Optional<Food> food = repo.findById(foodId);
        return food.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    //3. API xoá 1 món ăn theo ID
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFood(@PathVariable("id") Integer foodId) {
        if (repo.existsById(foodId)) {
            repo.deleteById(foodId);
            return ResponseEntity.ok("Deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Food not found");
        }
    }

    //4. API tạo mới 1 món ăn
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<Food> createFood(@RequestBody Food newFood) {
        Food savedFood = repo.save(newFood);
        return ResponseEntity.status(201).body(savedFood);
    }

    //5. API cập nhật thông tin món ăn
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Food> updateFood(@PathVariable("id") Integer foodId, @RequestBody Food updateFood) {
        Optional<Food> existingFood = repo.findById(foodId);
        if (existingFood.isPresent()) {
            Food food = existingFood.get();
            food.setName(updateFood.getName());
            food.setCategory(updateFood.getCategory());
            food.setPrice(updateFood.getPrice());
            food.setDescription(updateFood.getDescription());
            food.setImageFilename(updateFood.getImageFilename());
            Food updated = repo.save(food);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // 6. API test thử gọi service
    @GetMapping("/test")
    @ResponseBody
    public String testService() {
        List<Food> list = foodService.getAllFoods();
        return "Số lượng món ăn: " + list.size();
    }

}
