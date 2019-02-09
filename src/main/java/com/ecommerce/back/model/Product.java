package com.ecommerce.back.model;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String subTitle;
    private int price;
    private int stock;
    private int saleCount;
    private String imgUrls;
    private int categorySecondId;

    public Product(int id, String name, String subTitle, int price, int stock, int saleCount, String imgUrls, int categorySecondId) {
        this.id = id;
        this.name = name;
        this.subTitle = subTitle;
        this.price = price;
        this.stock = stock;
        this.saleCount = saleCount;
        this.imgUrls = imgUrls;
        this.categorySecondId = categorySecondId;
    }

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

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getCategorySecondId() {
        return categorySecondId;
    }

    public void setCategorySecondId(int categorySecondId) {
        this.categorySecondId = categorySecondId;
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
                Objects.equals(subTitle, product.subTitle) &&
                Objects.equals(imgUrls, product.imgUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subTitle, price, stock, saleCount, imgUrls);
    }
}
