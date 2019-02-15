package com.ecommerce.back.controller;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.BackApplication;
import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.CategoryFirst;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.util.JWTUtil;
import com.ecommerce.back.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackApplication.class)
@WebAppConfiguration
public class ProductControllerTest {
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryFirstDAO categoryFirstDAO;
    @Autowired
    private CategorySecondDAO categorySecondDAO;

    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .build();

        //添加测试用的1、2级分类
        categoryFirstDAO.addCategoryFirst(new CategoryFirst(-1, "分类1", 0));
        categorySecondDAO.addCategorySecond(new CategorySecond(-1, "分类1-1", 0, categoryFirstDAO.getCategoryFirstByName("分类1").getId()));
        categorySecondDAO.addCategorySecond(new CategorySecond(-1, "分类1-2", 0, categoryFirstDAO.getCategoryFirstByName("分类1").getId()));

        categoryFirstDAO.addCategoryFirst(new CategoryFirst(-1, "分类2", 0));
        categorySecondDAO.addCategorySecond(new CategorySecond(-1, "分类2-1", 0, categoryFirstDAO.getCategoryFirstByName("分类2").getId()));
        categorySecondDAO.addCategorySecond(new CategorySecond(-1, "分类2-2", 0, categoryFirstDAO.getCategoryFirstByName("分类2").getId()));
    }

    @After
    public void after() {
        categoryFirstDAO.deleteCategoryFirstByName("分类1");
        categorySecondDAO.deleteCategorySecondByName("分类1-1");
        categorySecondDAO.deleteCategorySecondByName("分类1-2");

        categoryFirstDAO.deleteCategoryFirstByName("分类2");
        categorySecondDAO.deleteCategorySecondByName("分类2-1");
        categorySecondDAO.deleteCategorySecondByName("分类2-2");
    }

    @Test
    public void productFunctionTest() throws Exception {
        RequestBuilder request;

        //无权限
        request = post("/product");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
        request = put("/product");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
        request = delete("/product");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        //有权限，手动测试
    }
}
