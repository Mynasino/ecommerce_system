package com.ecommerce.back.controller;

import com.ecommerce.back.model.CategoryFirst;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.CategoryService;
import com.ecommerce.back.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/category", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<CategoryFirst> getAllCategoryFirsts() {
        return categoryService.getAllCategoryFirsts();
    }

    @GetMapping("/first")
    public List<CategorySecond> getCategorySecondsOfSpecificCategoryFirst(@RequestParam("categoryFirstId") int categoryFirstId) {
        return categoryService.getCategorySecondsByCategoryFirstId(categoryFirstId);
    }

    @GetMapping("/second")
    public List<Product> getProductsOfSpecificCategorySecond(@RequestParam("categorySecondId") int categorySecondId,
                                                             @RequestParam("limit") int limit,
                                                             @RequestParam("offset") int offset) {
        return categoryService.getProductsByCIdLimitOffset(categorySecondId, limit, offset);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping("/first")
    public String addCategoryFirst(@RequestParam("categoryFirstName") String categoryFirstName, HttpServletResponse response) {
        String info = categoryService.addCategoryFirst(categoryFirstName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping("/first")
    public String deleteCategoryFirst(@RequestParam("categoryFirstName") String categoryFirstName, HttpServletResponse response) {
        String info = categoryService.deleteCategoryFirst(categoryFirstName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping("/second")
    public String addCategorySecond(@RequestParam("categorySecondName") String categorySecondName,
                                    @RequestParam("categoryFirstName") String categoryFirstName, HttpServletResponse response) {
        String info = categoryService.addCategorySecond(categorySecondName, categoryFirstName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }

    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping("/second")
    public String deleteCategorySecond(@RequestParam("categorySecondName") String categorySecondName, HttpServletResponse response) {
        String info = categoryService.deleteCategorySecond(categorySecondName);
        return ResponseUtil.SC_OKorSC_BAD_REQUESTResponse(info, response);
    }
}
