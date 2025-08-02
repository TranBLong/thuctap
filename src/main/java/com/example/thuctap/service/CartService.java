package com.example.thuctap.service;

import com.example.thuctap.models.Cart;
import com.example.thuctap.models.CartItem;
import com.example.thuctap.models.Food;
import com.example.thuctap.repository.CartItemRepository;
import com.example.thuctap.repository.CartRepository;
import com.example.thuctap.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {

    private Map<Integer, List<CartItem>> cart = new HashMap<>();

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodRepository foodRepository;

    public Cart getOrCreateCart(int customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerId(customerId);
                    newCart.setItems(new ArrayList<>()); // đảm bảo có danh sách rỗng
                    return cartRepository.save(newCart); // lưu luôn vào DB
                });
    }

    public BigDecimal getTotalAmount(Cart cart) {
        return cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getFood().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Cart addItemToCart(int customerId, int foodId, int quantity) {
        Cart cart = getOrCreateCart(customerId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getFood().getId() == foodId)
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found"));

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setFood(food);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public List<CartItem> getCartItems(int customerId) {
        Cart cart = getOrCreateCart(customerId);
        return cart.getItems();
    }

    public void increaseQuantity(int customerId, int foodId) {
        Cart cart = getOrCreateCart(customerId);
        for (CartItem item : cart.getItems()) {
            if (item.getFood().getId() == foodId) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
        cartRepository.save(cart);
    }

    public void decreaseQuantity(int customerId, int foodId) {
        Cart cart = getOrCreateCart(customerId);
        cart.getItems().removeIf(item -> {
            if (item.getFood().getId() == foodId) {
                int newQty = item.getQuantity() - 1;
                if (newQty <= 0) {
                    return true; // xóa luôn nếu về 0
                } else {
                    item.setQuantity(newQty);
                }
            }
            return false;
        });
        cartRepository.save(cart);
    }

    public void removeItem(int customerId, int foodId) {
        Cart cart = getOrCreateCart(customerId);
        cart.getItems().removeIf(item -> item.getFood().getId() == foodId);
        cartRepository.save(cart);
    }

    public double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

}
