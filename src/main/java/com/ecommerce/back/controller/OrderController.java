package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.exception.UnauthorizedException;
import com.ecommerce.back.model.Order;
import com.ecommerce.back.model.OrderItem;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("获取用户individualName的所有订单，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order")
    public List<Order> getOrdersOfSpecificUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        return orderService.getOrdersOfSpecificUser(individualName);
    }

    @ApiOperation("获取用户individualName的订单Id为orderId的订单下的所有订单项(orderItem)，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order/orderItem")
    public List<OrderItem> getOrderItemsOfSpecificOrder(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                                    @RequestParam("orderId") int orderId) throws IllegalException, UnauthorizedException {
        return orderService.getOrderItemsByOrderId(individualName, orderId);
    }

    @ApiOperation("获取用户individualName的购物车Id，需要在请求头放individualName的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/shoppingCart")
    public String getShoppingCartId(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        Integer shoppingCartId = orderService.getOrCreateShoppingCartIdByUserName(individualName);
        if (shoppingCartId == null) throw new IllegalException("用户名", individualName, " 创建购物车失败");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shoppingCartId", shoppingCartId);
        return jsonObject.toJSONString();
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
