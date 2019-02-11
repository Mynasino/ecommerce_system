package com.ecommerce.back.model;

import java.util.Arrays;
import java.util.Objects;

public class OrderComment {
    private int id;
    private String content;
    private String[] imgUrls;
    private int scoreLogistics;
    private int scoreQuality;
    private int scoreService;
    private int orderId;

    public OrderComment(int id, String content, String[] imgUrls, int scoreLogistics, int scoreQuality, int scoreService, int orderId) {
        this.id = id;
        this.content = content;
        this.imgUrls = imgUrls;
        this.scoreLogistics = scoreLogistics;
        this.scoreQuality = scoreQuality;
        this.scoreService = scoreService;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getScoreLogistics() {
        return scoreLogistics;
    }

    public void setScoreLogistics(int scoreLogistics) {
        this.scoreLogistics = scoreLogistics;
    }

    public int getScoreQuality() {
        return scoreQuality;
    }

    public void setScoreQuality(int scoreQuality) {
        this.scoreQuality = scoreQuality;
    }

    public int getScoreService() {
        return scoreService;
    }

    public void setScoreService(int scoreService) {
        this.scoreService = scoreService;
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
        OrderComment that = (OrderComment) o;
        return scoreLogistics == that.scoreLogistics &&
                scoreQuality == that.scoreQuality &&
                scoreService == that.scoreService &&
                orderId == that.orderId &&
                Objects.equals(content, that.content) &&
                Arrays.equals(imgUrls, that.imgUrls);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(content, scoreLogistics, scoreQuality, scoreService, orderId);
        result = 31 * result + Arrays.hashCode(imgUrls);
        return result;
    }
}
