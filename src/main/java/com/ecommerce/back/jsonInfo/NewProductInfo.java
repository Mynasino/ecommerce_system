package com.ecommerce.back.jsonInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "newProductInfo", description = "Information of new Product")
public class NewProductInfo {
    private String name;
    private String subTitle;
    private int price;
    private int stock;
    private int saleCount;
    @ApiModelProperty(value="(js)FileReader.toDataUrl,result.split(\",\")[1]", example = "[\"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\"]")
    private String[] imgBase64Strings;
    @ApiModelProperty(value="types of imgs, support png/jpg", example = "[\"png\"]")
    private String[] imgTypes;
    private String categorySecondName;

    public NewProductInfo() {}

    public NewProductInfo(String name, String subTitle, int price, int stock, int saleCount, String[] imgBase64Strings, String[] imgTypes, String categorySecondName) {
        this.name = name;
        this.subTitle = subTitle;
        this.price = price;
        this.stock = stock;
        this.saleCount = saleCount;
        this.imgBase64Strings = imgBase64Strings;
        this.imgTypes = imgTypes;
        this.categorySecondName = categorySecondName;
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

    public String getCategorySecondName() {
        return categorySecondName;
    }

    public void setCategorySecondName(String categorySecondName) {
        this.categorySecondName = categorySecondName;
    }
}
