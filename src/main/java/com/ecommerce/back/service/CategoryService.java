package com.ecommerce.back.service;

import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.model.CategoryFirst;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryFirstDAO categoryFirstDAO;
    private CategorySecondDAO categorySecondDAO;
    private ProductDAO productDAO;

    @Autowired
    public CategoryService(CategoryFirstDAO categoryFirstDAO, CategorySecondDAO categorySecondDAO, ProductDAO productDAO) {
        this.categoryFirstDAO = categoryFirstDAO;
        this.categorySecondDAO = categorySecondDAO;
        this.productDAO = productDAO;
    }

    public String addCategoryFirst(String name) {
        if (name.equals("")) return "category name can't be empty";
        if (categoryFirstDAO.getCategoryFirstByName(name) != null) return "categoryFirst " + name + " already exists";
        return (categoryFirstDAO.addCategoryFirst(new CategoryFirst(-1, name, 0)) > 0) ?
                "success" : "failed add categoryFirst " + name;
    }

    public String deleteCategoryFirst(String name) {
        return (categoryFirstDAO.deleteCategoryFirstByName(name) > 0) ? "success" : "failed delete categoryFirst " + name;
    }

    public List<CategoryFirst> getAllCategoryFirsts() {
        return categoryFirstDAO.getAllCategoryFirsts();
    }

    public String addCategorySecond(String categorySecondName, String categoryFirstName) {
        if (categorySecondName.equals("") || categoryFirstName.equals("")) return "category name can't be empty";
        CategoryFirst categoryFirst = categoryFirstDAO.getCategoryFirstByName(categoryFirstName);
        if (categoryFirst == null) return "categoryFirst " + categoryFirstName + " not exist";
        if (categorySecondDAO.getCategorySecondByName(categorySecondName) != null) return "categorySecond " + categorySecondName + " already exists";
        return (categorySecondDAO.addCategorySecond(new CategorySecond(-1, categorySecondName, 0, categoryFirst.getId())) > 0) ?
                "success" : "failed add categorySecond " + categorySecondName + " of categoryFirst " + categoryFirstName;
    }

    public String deleteCategorySecond(String categorySecondName) {
        return (categorySecondDAO.deleteCategorySecondByName(categorySecondName) > 0) ? "success" : "failed delete categorySecond " + categorySecondName;
    }

    public List<CategorySecond> getCategorySecondsByCategoryFirstId(int categoryFirstId) {
        return categorySecondDAO.getCategorySecondsByCategoryFirstId(categoryFirstId);
    }

    public List<Product> getProductsByCIdLimitOffset(int categorySecondId, int limit, int offset) {
        return productDAO.getProductsByCIdLimitOffset(categorySecondId, limit, offset);
    }
}
