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

    @GetMapping
    public String showProductList(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(name = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        List<Food> foodsList = repo.findAll();

        if (keyword != null && !keyword.isBlank()) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        if (minPrice != null) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getPrice() >= minPrice)
                    .toList();
        }
        if (maxPrice != null) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getPrice() <= maxPrice)
                    .toList();
        }

        if (minQuantity != null) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getQuantity() >= minQuantity)
                    .toList();
        }
        if (maxQuantity != null) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getQuantity() <= maxQuantity)
                    .toList();
        }

        // Lọc theo loại món
        if (category != null && !category.isBlank()) {
            foodsList = foodsList.stream()
                    .filter(f -> f.getCategory() != null && f.getCategory().equalsIgnoreCase(category))
                    .toList();
        }

        // Lấy danh sách loại món duy nhất
        List<String> categoryList = repo.findAllDistinctCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("selectedCategory", category); // để giữ selected

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

    //7. Sửa món ăn
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Integer id, Model model) {
        Optional<Food> optionalFood = repo.findById(id);
        if (optionalFood.isPresent()) {
            model.addAttribute("food", optionalFood.get());
            return "food/edit_food";
        } else {
            return "redirect:/food";
        }
    }

    //8. Cập nhật món ăn
    @PostMapping("/update")
    public String updateFood(@ModelAttribute("food") Food food) {
        repo.save(food);
        return "redirect:/food";
    }

    //9. Xoá món ăn
    @GetMapping("/delete")
    public String deleteFoodByLink(@RequestParam("id") Integer id) {
        repo.deleteById(id);
        return "redirect:/food";
    }

    //10. Thêm món ăn
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("food", new Food());
        return "food/create_food";
    }

    //11. Lưu món ăn đã được thêm
    @PostMapping("/save")
    public String saveFood(@ModelAttribute("food") Food food) {
        repo.save(food);
        return "redirect:/food";
    }
}
