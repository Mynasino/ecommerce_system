package com.ecommerce.back.jsonInfo;

import java.util.Date;

public class NewProductComment {
    private int productId;
    private String content;
    private String[] imgBase64Strings;
    private String[] imgTypes;
    private int score;
    private int orderId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImgBase64Strings() {
        return imgBase64Strings;
    }

    public void setImgBase64Strings(String[] imgBase64Strings) {
        this.imgBase64Strings = imgBase64Strings;
    }

    public String[] getImgTypes() {
        return imgTypes;
    }

    public void setImgTypes(String[] imgTypes) {
        this.imgTypes = imgTypes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
