package com.example.thuctap.repository;

import com.example.thuctap.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ✅ import dòng này để tránh lỗi
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);

    // ✅ Truy vấn lấy danh sách loại món không trùng nhau
    @Query("SELECT DISTINCT f.category FROM Food f WHERE f.category IS NOT NULL")
    List<String> findAllDistinctCategories();
}
