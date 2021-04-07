package com.example.mobileshopping;

public class Product {
    private String brand;
    private String productName;
    private double price;
    private boolean promotion;

    public String getBrand() {
        return brand;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public boolean getPromotion() {
        return promotion;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }
}
