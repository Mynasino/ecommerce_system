package com.ecommerce.back.model;

import java.util.Objects;

public class OrderComment {
    private int id;
    private String content;
    private String imgUrls;
    private int scoreLogistics;
    private int scoreQuality;
    private int scoreService;

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

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderComment that = (OrderComment) o;
        return scoreLogistics == that.scoreLogistics &&
                scoreQuality == that.scoreQuality &&
                scoreService == that.scoreService &&
                Objects.equals(content, that.content) &&
                Objects.equals(imgUrls, that.imgUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, imgUrls, scoreLogistics, scoreQuality, scoreService);
    }
}
