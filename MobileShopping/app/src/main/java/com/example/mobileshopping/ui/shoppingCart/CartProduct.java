package com.example.mobileshopping.ui.shoppingCart;

public class CartProduct {
    private String name;
    private int id;
    private int price;
    private int quantity;
    private int stone;

    public CartProduct(){}
    public CartProduct(String name, int id, int price, int quantity) {
        this.name=name;
        this.id=id;
        this.price=price;
        this.quantity=quantity;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }
}
