package com.ecommerce.back.model;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String subTitle;
    private int price;
    private int stock;
    private int saleCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price &&
                stock == product.stock &&
                saleCount == product.saleCount &&
                Objects.equals(name, product.name) &&
                Objects.equals(subTitle, product.subTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subTitle, price, stock, saleCount);
    }
}
