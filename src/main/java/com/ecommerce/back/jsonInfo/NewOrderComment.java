package com.ecommerce.back.jsonInfo;

public class NewOrderComment {
    private String content;
    private String[] imgBase64Strings;
    private String[] imgTypes;
    private int scoreLogistics;
    private int scoreQuality;
    private int scoreService;
    private int orderId;

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
}
