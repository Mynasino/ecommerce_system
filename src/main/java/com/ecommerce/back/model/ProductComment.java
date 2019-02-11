package com.ecommerce.back.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ProductComment {
    private int id;
    private int productId;
    private int userId;
    private String content;
    private String[] imgUrls;
    private int score;
    private Date createdTime;
    private int orderId;

    public ProductComment(int id, int productId, int userId, String content, String[] imgUrls, int score, Date createdTime, int orderId) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.content = content;
        this.imgUrls = imgUrls;
        this.score = score;
        this.createdTime = createdTime;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductComment that = (ProductComment) o;
        return productId == that.productId &&
                userId == that.userId &&
                score == that.score &&
                orderId == that.orderId &&
                Objects.equals(content, that.content) &&
                Arrays.equals(imgUrls, that.imgUrls) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId, content, imgUrls, score, createdTime, orderId);
    }
}
