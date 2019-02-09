package com.ecommerce.back.model;

import java.util.Objects;

public class CategoryFirst {
    private int id;
    private String name;
    private int count;

    public CategoryFirst(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryFirst that = (CategoryFirst) o;
        return count == that.count &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count);
    }
}
