package com.example.thuctap.controller;

import com.example.thuctap.models.Cart;
import com.example.thuctap.models.CartItem;
import com.example.thuctap.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartViewController {

    @Autowired
    private CartService cartService;

    @GetMapping("/view/{customerId}")
    public String viewCart(@PathVariable int customerId, Model model) {
        List<CartItem> items = cartService.getCartItems(customerId);
        model.addAttribute("cartItems", items);
        model.addAttribute("customerId", customerId);
        return "cart/view-cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam int customerId,
                            @RequestParam int foodId,
                            @RequestParam int quantity) {
        cartService.addItemToCart(customerId, foodId, quantity);
        return "redirect:/cart/view/" + customerId;
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam int customerId,
                                 @RequestParam int foodId) {
        cartService.removeItem(customerId, foodId);
        return "redirect:/cart/view/" + customerId;
    }

    @GetMapping("/update")
    public String updateQuantity(@RequestParam int customerId,
                                 @RequestParam int foodId,
                                 @RequestParam String action) {
        if ("increase".equals(action)) {
            cartService.increaseQuantity(customerId, foodId);
        } else if ("decrease".equals(action)) {
            cartService.decreaseQuantity(customerId, foodId);
        }
        return "redirect:/cart/view/" + customerId;
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("customerId") int customerId, Model model) {
        Cart cart = cartService.getOrCreateCart(customerId);

        if (!cart.getItems().isEmpty()) {
            cart.getItems().clear(); // Xoá tất cả item
            cartService.saveCart(cart); // Lưu lại giỏ rỗng
        }

        model.addAttribute("message", "Thanh toán thành công!");
        model.addAttribute("customerId", customerId); // ✅ truyền customerId để dùng trong link
        return "cart/checkout-success";
    }

}
