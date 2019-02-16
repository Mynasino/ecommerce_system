package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.exception.UnauthorizedException;
import com.ecommerce.back.jsonInfo.NewOrderComment;
import com.ecommerce.back.jsonInfo.NewProductComment;
import com.ecommerce.back.model.OrderComment;
import com.ecommerce.back.model.ProductComment;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.CommentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("新增用户individualName对订单的评论，评论信息写在请求体NewOrderComment中")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/order")
    public void addOrderComment(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                   @RequestBody NewOrderComment newOrderComment) throws IllegalException, UnauthorizedException, IOException {
        commentService.addOrderComment(individualName, newOrderComment);
    }

    @ApiOperation("新增用户individualName对商品的评论，评论信息写在请求体NewOrderComment中")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @PutMapping("/product")
    public void addProductComment(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                  @RequestBody NewProductComment newProductComment) throws IllegalException, UnauthorizedException, IOException {
        commentService.addProductComment(individualName, newProductComment);
    }

    @ApiOperation("获取用户IndividualName对订单Id为orderId的订单的评论，需要在请求头放该用户的token")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/order/user")
    public OrderComment getOrderCommentByOrderId(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName,
                                        @RequestParam("orderId") int orderId) throws IllegalException, UnauthorizedException  {
        return commentService.getOrderComment(individualName, orderId);
    }

    @ApiOperation("获取用户IndividualName的所有商品评论")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {true})
    @GetMapping("/product/user")
    public List<ProductComment> getProductCommentsByUserName(@RequestParam(JWTUtil.SPECIFIC_PARAM_NAME) String individualName) throws IllegalException {
        return commentService.getProductCommentsByUserName(individualName);
    }

    @ApiOperation("获取商品Id为productId的所有评论，跳过offset条，最多取limit条(分页)")
    @GetMapping("/product")
    public List<ProductComment> getProductCommentsByProductId(@RequestParam("productId") int productId,
                                                               @RequestParam("limit") int limit,
                                                               @RequestParam("offset") int offset) {
        return commentService.getProductCommentsByProductId(productId, limit, offset);
    }
}