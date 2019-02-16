package com.ecommerce.back.controller;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.model.CategoryFirst;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.AuthenticationRequired;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("获取所有一级分类")
    @GetMapping("/")
    public List<CategoryFirst> getAllCategoryFirsts() {
        return categoryService.getAllCategoryFirsts();
    }

    @ApiOperation("获取一级分类Id为categoryFirstId下属的所有二级分类")
    @GetMapping("/first")
    public List<CategorySecond> getCategorySecondsOfSpecificCategoryFirst(@RequestParam("categoryFirstId") int categoryFirstId) {
        return categoryService.getCategorySecondsByCategoryFirstId(categoryFirstId);
    }

    @ApiOperation("获取二级分类Id为categorySecondId下属的所有商品，跳过offset条，最多取limit条(分页)")
    @GetMapping("/second")
    public List<Product> getProductsOfSpecificCategorySecond(@RequestParam("categorySecondId") int categorySecondId,
                                                             @RequestParam("limit") int limit,
                                                             @RequestParam("offset") int offset) {
        return categoryService.getProductsByCIdLimitOffset(categorySecondId, limit, offset);
    }

    @ApiOperation("新增名为categoryFirstName的一级分类")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping("/first")
    public void addCategoryFirst(@RequestParam("categoryFirstName") String categoryFirstName) throws IllegalException {
        categoryService.addCategoryFirst(categoryFirstName);
    }

    @ApiOperation("删除名为categoryFirstName的一级分类")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping("/first")
    public void deleteCategoryFirst(@RequestParam("categoryFirstName") String categoryFirstName) {
        categoryService.deleteCategoryFirst(categoryFirstName);
    }

    @ApiOperation("新增名为categorySecondName的二级分类，其一级分类名为categoryFirstName")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @PostMapping("/second")
    public void addCategorySecond(@RequestParam("categorySecondName") String categorySecondName,
                                    @RequestParam("categoryFirstName") String categoryFirstName) throws IllegalException {
        categoryService.addCategorySecond(categorySecondName, categoryFirstName);
    }

    @ApiOperation("删除名为categorySecondName的二级分类")
    @ApiImplicitParam(paramType = "header", name = JWTUtil.HEADER_KEY, required = true)
    @AuthenticationRequired(levels = {AuthenticationLevel.ADMIN}, specifics = {false})
    @DeleteMapping("/second")
    public void deleteCategorySecond(@RequestParam("categorySecondName") String categorySecondName) {
        categoryService.deleteCategorySecond(categorySecondName);
    }
}
