package com.example.thuctap.repository;

import com.example.thuctap.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByCustomer_Id(int customerId);
}
