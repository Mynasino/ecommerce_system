package com.ecommerce.back.controller;

import com.ecommerce.back.BackApplication;
import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
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
public class CategoryControllerTest {
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
    }

    @Test
    public void addCategoryTest() throws Exception {
        RequestBuilder request;

        //无管理员权限
        request = post("/category/first")
                .param("categoryFirstName", "分类1");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
        request = post("/category/second")
                .param("categorySecondName", "分类1-1")
                .param("categoryFirstName", "分类1");
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        //获取管理员权限
        String adminJWT = JWTUtil.getJWTString("admin", AuthenticationLevel.ADMIN, new Date());

        //第一次添加一级分类
        request = post("/category/first")
                .header(JWTUtil.HEADER_KEY, adminJWT)
                .param("categoryFirstName", "分类1");
        mockMvc.perform(request)
                .andExpect(status().isOk());

        //重复添加
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());

        //添加一级分类不存在的二级分类
        request = post("/category/second")
                .header(JWTUtil.HEADER_KEY, adminJWT)
                .param("categorySecondName", "分类1-1")
                .param("categoryFirstName", "分类2");
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());

        //第一次添加二级分类
        request = post("/category/second")
                .header(JWTUtil.HEADER_KEY, adminJWT)
                .param("categorySecondName", "分类1-1")
                .param("categoryFirstName", "分类1");
        mockMvc.perform(request)
                .andExpect(status().isOk());

        //重复添加二级分类
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @After
    public void after() {
        categoryFirstDAO.deleteCategoryFirstByName("分类1");
        categorySecondDAO.deleteCategorySecondByName("分类1-1");
    }
}
