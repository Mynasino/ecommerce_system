package com.ecommerce.back.jsonInfo;

import com.ecommerce.back.model.Order;
import com.ecommerce.back.model.OrderItem;
import com.ecommerce.back.model.Product;

import java.util.Date;
import java.util.List;

public class OrderInfo {
    private int id;
    private String address;
    private String mobile;
    private Date createTime;
    private Date payTime;
    private Date deliveryTime;
    private int userId;
    private int statusCode;
    private List<Product> products;
    private List<Integer> productCounts;

    public OrderInfo() {}

    public OrderInfo(Order order, List<Product> products, List<Integer> productCounts) {
        this.id = order.getId();
        this.address = order.getAddress();
        this.mobile = order.getMobile();
        this.createTime = order.getCreateTime();
        this.payTime = order.getPayTime();
        this.deliveryTime = order.getDeliveryTime();
        this.userId = order.getUserId();
        this.statusCode = order.getStatusCode();
        this.products = products;
        this.productCounts = productCounts;
    }

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getProductCounts() {
        return productCounts;
    }

    public void setProductCounts(List<Integer> productCounts) {
        this.productCounts = productCounts;
    }
}
