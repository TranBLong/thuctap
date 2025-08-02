package com.example.thuctap.repository;

import com.example.thuctap.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCartId(int cartId);
    CartItem findByCustomerIdAndFoodId(int customerId, int foodId);
}