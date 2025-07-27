package com.example.thuctap.service;

import com.example.thuctap.models.Food;
import com.example.thuctap.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Optional<Food> getFoodById(int id) {
        return foodRepository.findById(id);
    }

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    public boolean deleteFood(int id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Food> updateFood(int id, Food update) {
        Optional<Food> old = foodRepository.findById(id);
        if (old.isPresent()) {
            Food food = old.get();
            food.setName(update.getName());
            food.setCategory(update.getCategory());
            food.setPrice(update.getPrice());
            food.setDescription(update.getDescription());
            food.setImageFilename(update.getImageFilename());
            return Optional.of(foodRepository.save(food));
        }
        return Optional.empty();
    }
}
