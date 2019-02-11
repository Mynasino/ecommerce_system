package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecommerce.back.model.Order;
import com.ecommerce.back.model.OrderItem;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.OrderService;
import com.ecommerce.back.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order")
    public List<Order> getOrdersOfSpecificUser(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName, HttpServletResponse response) {
        return orderService.getOrdersOfSpecificUser(individualName);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order/orderItem")
    public List<OrderItem> getOrderItemsOfSpecificOrder(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                                    @RequestParam("orderId") int orderId, HttpServletResponse response) {
        return orderService.getOrderItemsByOrderId(individualName, orderId);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/shoppingCart")
    public String getShoppingCartId(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName, HttpServletResponse response) {
        try {
            Integer shoppingCartId = orderService.getOrCreateShoppingCartIdByUserName(individualName);
            if (shoppingCartId == null)
                throw new IllegalStateException("create shoppingCart for " + individualName + " failed");
            else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shoppingCartId", shoppingCartId);
                return jsonObject.toJSONString();
            }
        } catch (IllegalStateException e) {
            return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(e.getMessage(), response);
        }
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping("/shoppingCart")
    public String addOrderItemToShoppingCart(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                              @RequestParam("shoppingCartId")int shoppingCartId,
                                              @RequestParam("productId") int productId,
                                              @RequestParam("count") int count, HttpServletResponse response) {
        String info = orderService.addOrderItemToShoppingCart(individualName, shoppingCartId, productId, count);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PatchMapping("/shoppingCart/orderItem")
    public String modifyOrderItemCount(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                        @RequestParam("shoppingCartId")int shoppingCartId,
                                        @RequestParam("productId") int productId,
                                        @RequestParam("count") int count, HttpServletResponse response) {
        String info = orderService.modifyOrderItemCount(individualName, shoppingCartId, productId, count);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
}

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @DeleteMapping("/shoppingCart")
    public String deleteOrderItemInShoppingCart(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                                 @RequestParam("shoppingCartId") int shoppingCartId,
                                                 @RequestParam("productId") int productId, HttpServletResponse response) {
        String info = orderService.deleteOrderItemInShoppingCart(individualName, shoppingCartId, productId);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/shoppingCart")
    public String submitShoppingCartAsOrder(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                             @RequestParam("shoppingCartId") int shoppingCartId,
                                             @RequestParam("address") String address,
                                             @RequestParam("mobile") String mobile, HttpServletResponse response) {
        String info = orderService.submitShoppingCart(individualName, shoppingCartId, address, mobile);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }
}
