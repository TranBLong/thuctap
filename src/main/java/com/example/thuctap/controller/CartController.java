package com.example.thuctap.controller;

import com.example.thuctap.models.Cart;
import com.example.thuctap.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCart(@PathVariable int customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(
            @RequestParam int customerId,
            @RequestParam int menuId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(customerId, menuId, quantity));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable int itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Cart> clearCart(@PathVariable int customerId) {
        return ResponseEntity.ok(cartService.clearCart(customerId));
    }
}
