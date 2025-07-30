package com.example.thuctap.service;

import com.example.thuctap.models.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {
    List<Food> getAllFoods();
    Optional<Food> getFoodById(int id);
    Food saveFood(Food food);
    boolean deleteFood(int id);
    Optional<Food> updateFood(int id, Food update);
    List<Food> getFoodsByIds(List<Integer> ids);  // ✅ đây mới đúng
}
