package com.ecommerce.back.service;

import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.exception.IllegalException;
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

    /**
     * 新增一级分类，通过数据库UQ约束防止重复分类名
     * @param name 一级分类名
     * @throws IllegalException 参数不合法
     */
    public void addCategoryFirst(String name) throws IllegalException {
        if (name == null || name.equals("")) throw new IllegalException("一级分类名", name, "不能为空");
        categoryFirstDAO.addCategoryFirst(new CategoryFirst(-1, name, 0));
    }

    /**
     * 删除一级分类
     * @param name 要删除的一级分类名
     */
    public void deleteCategoryFirst(String name) {
        categoryFirstDAO.deleteCategoryFirstByName(name);
    }

    /**
     * 获得所有一级分类
     * @return 一级分类列表
     */
    public List<CategoryFirst> getAllCategoryFirsts() {
        return categoryFirstDAO.getAllCategoryFirsts();
    }

    /**
     * 添加二级分类
     * @param categorySecondName 二级分类名
     * @param categoryFirstName 二级分类所属一级分类名
     * @throws IllegalException 参数不合法
     */
    public void addCategorySecond(String categorySecondName, String categoryFirstName) throws IllegalException {
        if (categorySecondName == null || categorySecondName.equals("")) throw new IllegalException("二级分类名", categorySecondName, "不能为空");
        if (categoryFirstName == null || categoryFirstName.equals("")) throw new IllegalException("一级分类名", categoryFirstName, "不能为空");
        CategoryFirst categoryFirst = categoryFirstDAO.getCategoryFirstByName(categoryFirstName);
        if (categoryFirst == null) throw new IllegalException("一级分类名", categoryFirstName, "不存在");
        categorySecondDAO.addCategorySecond(new CategorySecond(-1, categorySecondName, 0, categoryFirst.getId()));
    }

    /**
     * 删除二级分类
     * @param categorySecondName 要删除的二级分类名
     */
    public void deleteCategorySecond(String categorySecondName) {
        categorySecondDAO.deleteCategorySecondByName(categorySecondName);
    }

    /**
     * 获取指定一级分类下的所有二级分类
     * @param categoryFirstId 一级分类Id
     * @return 该一级分类下的所有二级分类
     */
    public List<CategorySecond> getCategorySecondsByCategoryFirstId(int categoryFirstId) {
        return categorySecondDAO.getCategorySecondsByCategoryFirstId(categoryFirstId);
    }

    /**
     * 获取指定二级分类下的从offset条开始的limit条商品
     * @param categorySecondId 二级分类Id
     * @param limit 最多limit条
     * @param offset 从offset条开始取
     * @return 商品列表
     */
    public List<Product> getProductsByCIdLimitOffset(int categorySecondId, int limit, int offset) {
        return productDAO.getProductsByCIdLimitOffset(categorySecondId, limit, offset);
    }
}
