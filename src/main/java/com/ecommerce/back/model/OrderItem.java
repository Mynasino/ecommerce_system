package com.ecommerce.back.model;

import java.util.Objects;

public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int count;

    public OrderItem(int id, int orderId, int productId, int count) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        OrderItem orderItem = (OrderItem) o;
        return orderId == orderItem.orderId &&
                productId == orderItem.productId &&
                count == orderItem.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId, count);
    }
}
