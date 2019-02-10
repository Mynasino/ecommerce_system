package com.ecommerce.back.service;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class ProductService {
    private ProductDAO productDAO;
    private CategorySecondDAO categorySecondDAO;
    private CategoryFirstDAO categoryFirstDAO;

    @Autowired
    public ProductService(ProductDAO productDAO, CategorySecondDAO categorySecondDAO, CategoryFirstDAO categoryFirstDAO) {
        this.productDAO = productDAO;
        this.categorySecondDAO = categorySecondDAO;
        this.categoryFirstDAO = categoryFirstDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String addProduct(NewProductInfo newProductInfo) throws IOException, IllegalStateException  {
        if (!ProductUtil.isLegalNewProductInfo(newProductInfo)) return "illegal new product information";
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) return "categorySecond " + newProductInfo.getCategorySecondName() + " not exist";
        if (productDAO.getProductByName(newProductInfo.getName()) != null) return "product name " + newProductInfo.getName() + " already exist";

        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();

        String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);
        Product newProduct = new Product(-1, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                newProductInfo.getStock(), newProductInfo.getSaleCount(), imgUrls, categorySecond.getId());

        //add new Product and add new categorySecond's and categoryFirst's product count
        //Unified throw exceptions
        if (productDAO.addProduct(newProduct) == 0)
            throw new IllegalStateException("failed to add new Product");
        if (categorySecondDAO.addProductCount(categorySecond.getId()) == 0)
            throw new IllegalStateException("failed to add categorySecond " + categorySecond.getName() + " Count");
        if (categoryFirstDAO.addProductCount(categorySecond.getCategoryFirstId()) == 0)
            throw new IllegalStateException("failed to add categoryFirst with id:" + categorySecond.getCategoryFirstId() + " Count");

        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String modifyProduct(NewProductInfo newProductInfo, int productId) throws IOException, IllegalStateException {
        if (!ProductUtil.isLegalNewProductInfo(newProductInfo)) return "illegal new product information";

        //get the origin Product and CategorySecond
        Product originProduct = productDAO.getProductById(productId);
        if (originProduct == null) return "product id " + productId + " not exist";
        //if product's name has changed, ensure not conflict with other product
        if (!newProductInfo.getName().equals(originProduct.getName()))
            if (productDAO.getProductByName(newProductInfo.getName()) != null)
                return "product with name: " + newProductInfo.getName() + " already exist";
        CategorySecond originCategorySecond = categorySecondDAO.getCategorySecondById(originProduct.getCategorySecondId());

        //get the new CategorySecond
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) return "categorySecond " + newProductInfo.getCategorySecondName() + " not exist";

        //store and construct the new Product
        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();
        String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);
        Product newProduct = new Product(productId, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                newProductInfo.getStock(), newProductInfo.getSaleCount(), imgUrls, categorySecond.getId());

        //if origin CategorySecond exist, reduce
        if (originCategorySecond != null) {
            if (categorySecondDAO.reduceProductCount(originCategorySecond.getId()) == 0)
                throw new IllegalStateException("failed to reduce categorySecond " + originCategorySecond.getName() + " Count");
            if (categoryFirstDAO.reduceProductCount(originCategorySecond.getCategoryFirstId()) == 0)
                throw new IllegalStateException("failed to reduce categoryFirst with id:" + originCategorySecond.getCategoryFirstId() + " Count");
        }
        //add new Product and add new categorySecond's and categoryFirst's product count
        if (productDAO.updateProduct(newProduct) == 0)
            throw new IllegalStateException("failed to add new Product");
        if (categorySecondDAO.addProductCount(categorySecond.getId()) == 0)
            throw new IllegalStateException("failed to add categorySecond " + categorySecond.getName() + " Count");
        if (categoryFirstDAO.addProductCount(categorySecond.getCategoryFirstId()) == 0)
            throw new IllegalStateException("failed to add categoryFirst with id:" + categorySecond.getCategoryFirstId() + " Count");

        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String deleteProduct(String productName) throws IllegalStateException {
        Product product = productDAO.getProductByName(productName);
        if (product == null) return "no product with name " + productName;
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondById(product.getCategorySecondId());

        if (categorySecond != null) {
            if (categorySecondDAO.reduceProductCount(categorySecond.getId()) == 0)
                throw new IllegalStateException("failed to reduce categorySecond " + categorySecond.getName() + " Count");
            if (categoryFirstDAO.reduceProductCount(categorySecond.getCategoryFirstId()) == 0)
                throw new IllegalStateException("failed to reduce categoryFirst with id:" + categorySecond.getCategoryFirstId() + " Count");
        }
        if (productDAO.deleteProductByName(productName) == 0)
            throw new IllegalStateException("failed to delete product " + productName);

        return "success";
    }

    public Product getProductByProductId(int productId) {
        return productDAO.getProductById(productId);
    }
}
