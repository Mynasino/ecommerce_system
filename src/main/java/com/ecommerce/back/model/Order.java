package com.ecommerce.back.model;

import java.util.Date;
import java.util.Objects;

public class Order {
    private int id;
    private String address;
    private String customerName;
    private String mobile;
    private Date createTime;
    private Date payTime;
    private Date deliveryTime;
    private int userId;
    private int statusCode;
    private int categorySecondId;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public int getCategorySecondId() {
        return categorySecondId;
    }

    public void setCategorySecondId(int categorySecondId) {
        this.categorySecondId = categorySecondId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return userId == order.userId &&
                statusCode == order.statusCode &&
                categorySecondId == order.categorySecondId &&
                Objects.equals(address, order.address) &&
                Objects.equals(customerName, order.customerName) &&
                Objects.equals(mobile, order.mobile) &&
                Objects.equals(createTime, order.createTime) &&
                Objects.equals(payTime, order.payTime) &&
                Objects.equals(deliveryTime, order.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, customerName, mobile, createTime, payTime, deliveryTime, userId, statusCode, categorySecondId);
    }
}
