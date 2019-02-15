package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/product", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping
    public void addProduct(@RequestBody NewProductInfo newProductInfo) throws IllegalException, IOException {
        productService.addProduct(newProductInfo);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping
    public void deleteProduct(@RequestParam("productName") String productName) throws IllegalException {
        productService.deleteProduct(productName);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PutMapping
    public void modifyProduct(@RequestBody NewProductInfo newProductInfo,
                                @RequestParam("productId") int productId) throws IllegalException, IOException {
        productService.modifyProduct(newProductInfo, productId);
    }

    @GetMapping
    public Product getProduct(@RequestParam("productId") int productId) throws IllegalException {
        Product product = productService.getProductByProductId(productId);
        if (product == null) throw new IllegalException("商品Id", productId + "", "不存在");
        return product;
    }
}
