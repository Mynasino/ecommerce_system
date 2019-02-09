package com.ecommerce.back.model;

import java.util.Objects;

public class CategorySecond {
    private int id;
    private String name;
    private int count;
    private int categoryFirstId;

    public CategorySecond(int id, String name, int count, int categoryFirstId) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.categoryFirstId = categoryFirstId;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCategoryFirstId() {
        return categoryFirstId;
    }

    public void setCategoryFirstId(int categoryFirstId) {
        this.categoryFirstId = categoryFirstId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorySecond that = (CategorySecond) o;
        return count == that.count &&
                categoryFirstId == that.categoryFirstId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count, categoryFirstId);
    }
}
