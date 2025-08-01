package com.example.thuctap.service;

import com.example.thuctap.models.Cart;
import com.example.thuctap.models.CartItem;
import com.example.thuctap.models.Customer;
import com.example.thuctap.models.Menu;
import com.example.thuctap.repository.CartItemRepository;
import com.example.thuctap.repository.CartRepository;
import com.example.thuctap.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired private MenuRepository menuRepository;

    public Cart getCartByCustomerId(int customerId) {
        return cartRepository.findByCustomer_Id(customerId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setCustomer(new Customer(customerId));
            return cartRepository.save(cart);
        });
    }

    public Cart addItemToCart(int customerId, int menuId, int quantity) {
        Cart cart = getCartByCustomerId(customerId);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại"));

        // ✅ Tính tổng giá món ăn trong menu
        double menuPrice = 0;
        for (var food : menu.getFoods()) {
            menuPrice += food.getPrice();
        }

        CartItem item = new CartItem();
        item.setMenuItem(menu);
        item.setQuantity(quantity);
        item.setPrice(menuPrice * quantity); // ✅ Gán giá cho item
        item.setCart(cart);

        cart.getItems().add(item);
        cart.setTotalPrice(cart.getTotalPrice() + item.getPrice());

        cartRepository.save(cart);
        return cart;
    }


    public Cart removeItem(int cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục"));
        Cart cart = item.getCart();
        cart.setTotalPrice(cart.getTotalPrice() - item.getPrice());
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        return cartRepository.save(cart);
    }

    public Cart clearCart(int customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }
}
