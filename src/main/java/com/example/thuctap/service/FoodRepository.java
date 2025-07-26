package com.example.thuctap.service;


import com.example.thuctap.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FoodRepository extends JpaRepository<Food, Integer> {


}
