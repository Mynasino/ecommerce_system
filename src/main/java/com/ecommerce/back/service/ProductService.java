package com.ecommerce.back.service;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private ProductDAO productDAO;
    private CategorySecondDAO categorySecondDAO;

    @Autowired
    public ProductService(ProductDAO productDAO, CategorySecondDAO categorySecondDAO) {
        this.productDAO = productDAO;
        this.categorySecondDAO = categorySecondDAO;
    }

    public String addProduct(NewProductInfo newProductInfo) {
        if (!ProductUtil.isLegalNewProductInfo(newProductInfo)) return "illegal new product information";
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) return "categorySecond " + newProductInfo.getCategorySecondName() + " not exist";
        if (productDAO.getProductByName(newProductInfo.getName()) != null) return "product name " + newProductInfo.getName() + " already exist";

        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();
        try {
            String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);
            Product newProduct = new Product(-1, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                    newProductInfo.getStock(), newProductInfo.getSaleCount(), JSON.toJSONString(imgUrls), categorySecond.getId());

            return (productDAO.addProduct(newProduct) > 0) ? "success" : "failed to add new Product";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String modifyProduct(NewProductInfo newProductInfo, int productId) {
        if (productDAO.getProductById(productId) == null) return "product id " + productId + " not exist";

        if (!ProductUtil.isLegalNewProductInfo(newProductInfo)) return "illegal new product information";
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) return "categorySecond " + newProductInfo.getCategorySecondName() + " not exist";

        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();
        try {
            String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);
            Product newProduct = new Product(productId, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                    newProductInfo.getStock(), newProductInfo.getSaleCount(), JSON.toJSONString(imgUrls), categorySecond.getId());

            return (productDAO.updateProduct(newProduct) > 0) ? "success" : "failed to add new Product";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteProduct(String productName) {
        return (productDAO.deleteProductByName(productName) > 0) ? "success" : "failed to delete product " + productName;
    }

    public Product getProductByProductId(int productId) {
        return productDAO.getProductById(productId);
    }
}
