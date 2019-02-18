package com.ecommerce.back.jsonInfo;

import com.ecommerce.back.model.CategorySecond;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("一级分类详情")
public class CategoryFirstDetail {
    private int id;
    private String name;
    private int count;
    private List<CategorySecond> categorySeconds;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CategorySecond> getCategorySeconds() {
        return categorySeconds;
    }

    public void setCategorySeconds(List<CategorySecond> categorySeconds) {
        this.categorySeconds = categorySeconds;
    }
}
