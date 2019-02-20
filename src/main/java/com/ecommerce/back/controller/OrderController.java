package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.exception.UnauthorizedException;
import com.ecommerce.back.jsonInfo.OrderInfo;
import com.ecommerce.back.model.Order;
import com.ecommerce.back.model.OrderItem;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.OrderService;
import com.ecommerce.back.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {
    private OrderService orderService;
    private ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    private OrderInfo getOrderInfoByOrder(Order order) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(order.getId());
        List<Product> products = new ArrayList<>();
        List<Integer> productCounts = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = productService.getProductByProductId(orderItem.getProductId());
            if (product != null) {
                products.add(product);
                productCounts.add(orderItem.getCount());
            }
        }

        return new OrderInfo(order, products, productCounts);
    }

    @ApiOperation("获取用户individualName的所有订单，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order")
    public List<OrderInfo> getOrdersOfSpecificUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        List<Order> orders = orderService.getOrdersOfSpecificUser(individualName);
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (Order order : orders)
            orderInfos.add(
                    getOrderInfoByOrder(order)
            );

        return orderInfos;
    }

    @ApiOperation("获取用户individualName的购物车，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/shoppingCart")
    public OrderInfo getShoppingCartId(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        Order shoppingCart = orderService.getOrCreateShoppingCartIdByUserName(individualName);
        if (shoppingCart == null) throw new IllegalException("用户名", individualName, " 创建购物车失败");

        return getOrderInfoByOrder(shoppingCart);
    }

    @ApiOperation("向购物车Id为shoppingCartId的购物车添加count个商品(初次加购物车)，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping("/shoppingCart")
    public void addOrderItemToShoppingCart(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                              @RequestParam("shoppingCartId")int shoppingCartId,
                                              @RequestParam("productId") int productId,
                                              @RequestParam("count") int count) throws IllegalException, UnauthorizedException {
        orderService.addOrderItemToShoppingCart(individualName, shoppingCartId, productId, count);
    }

    @ApiOperation("加购物车后修改商品购买数量，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping("/shoppingCart/orderItem")
    public void modifyOrderItemCount(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                        @RequestParam("shoppingCartId")int shoppingCartId,
                                        @RequestParam("productId") int productId,
                                        @RequestParam("count") int count) throws IllegalException, UnauthorizedException {
        orderService.modifyOrderItemCount(individualName, shoppingCartId, productId, count);
    }

    @ApiOperation("删除购物车里的某个商品，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @DeleteMapping("/shoppingCart")
    public void deleteOrderItemInShoppingCart(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                                 @RequestParam("shoppingCartId") int shoppingCartId,
                                                 @RequestParam("productId") int productId) throws IllegalException, UnauthorizedException {
        orderService.deleteOrderItemInShoppingCart(individualName, shoppingCartId, productId);
    }

    @ApiOperation("购物车里的提交订单功能，需要带上地址和手机号，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/shoppingCart")
    public void submitShoppingCartAsOrder(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                             @RequestParam("shoppingCartId") int shoppingCartId,
                                             @RequestParam("address") String address,
                                             @RequestParam("mobile") String mobile) throws IllegalException, UnauthorizedException {
        orderService.submitShoppingCart(individualName, shoppingCartId, address, mobile);
    }
}
