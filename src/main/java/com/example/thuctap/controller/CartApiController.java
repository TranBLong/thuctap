package com.example.thuctap.controller;

import com.example.thuctap.models.Cart;
import com.example.thuctap.models.CartItem;
import com.example.thuctap.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestParam int customerId,
                                        @RequestParam int foodId,
                                        @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(customerId, foodId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItem(@RequestParam int customerId,
                                        @RequestParam int foodId) {
        cartService.removeItem(customerId, foodId);
        return ResponseEntity.ok("Item removed");
    }
}
