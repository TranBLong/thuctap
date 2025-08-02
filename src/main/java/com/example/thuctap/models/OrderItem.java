package com.example.thuctap.models;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ✅ Khóa chính cho từng item

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // ✅ Mỗi item thuộc về một Order

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food; // ✅ Giả sử bạn có entity Food để đại diện món ăn

    private int quantity;
    private double price;

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
