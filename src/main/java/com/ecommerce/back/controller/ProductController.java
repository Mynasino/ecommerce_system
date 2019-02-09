package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.ProductService;
import com.ecommerce.back.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/product", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping
    public String addProduct(@RequestBody NewProductInfo newProductInfo, HttpServletResponse response) {
        String info = productService.addProduct(newProductInfo);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping
    public String deleteProduct(@RequestParam("productName") String productName, HttpServletResponse response) {
        String info = productService.deleteProduct(productName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PutMapping
    public String modifyProduct(@RequestBody NewProductInfo newProductInfo,
                                @RequestParam("productId") int productId, HttpServletResponse response) {
        String info = productService.modifyProduct(newProductInfo, productId);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @GetMapping
    public String getProduct(@RequestParam("productId") int productId, HttpServletResponse response) {
        Product product = productService.getProductByProductId(productId);
        return (product == null) ? ResponseUtil.SC_OKorSC_BAD_REQUESTResponse("no product with id " + productId, response) :
                JSON.toJSONString(product);
    }
}
