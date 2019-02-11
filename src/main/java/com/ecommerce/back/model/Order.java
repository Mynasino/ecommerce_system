package com.ecommerce.back.model;

import java.util.Date;
import java.util.Objects;

public class Order {
    private int id;
    private String address;
    private String mobile;
    private Date createTime;
    private Date payTime;
    private Date deliveryTime;
    private int userId;
    private int statusCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return userId == order.userId &&
                statusCode == order.statusCode &&
                Objects.equals(address, order.address) &&
                Objects.equals(mobile, order.mobile) &&
                Objects.equals(createTime, order.createTime) &&
                Objects.equals(payTime, order.payTime) &&
                Objects.equals(deliveryTime, order.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, mobile, createTime, payTime, deliveryTime, userId, statusCode);
    }
}
