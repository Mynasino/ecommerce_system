package com.ecommerce.back.service;

import com.alibaba.fastjson.JSON;
import com.ecommerce.back.dao.CategoryFirstDAO;
import com.ecommerce.back.dao.CategorySecondDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.NewProductInfo;
import com.ecommerce.back.model.CategorySecond;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

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

    /**
     * 根据新增商品所需信息(newProductInfo)来新增商品，商品名不能重复
     * @param newProductInfo 新增商品所需信息
     * @throws IllegalException 信息不合法
     * @throws IOException 上传图片IOException
     */
    @Transactional(propagation = Propagation.REQUIRED) //需要事务来保证新增商品，增加对应一级分类，二级分类的商品数同时生效/失效
    public void addProduct(NewProductInfo newProductInfo) throws IllegalException ,IOException {
        ProductUtil.checkNewProductInfoLegality(newProductInfo);

        //获取新商品的二级分类
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) throw new IllegalException("二级分类", newProductInfo.getCategorySecondName(), "不存在");

        //如果商品名不存在，则上传多张图片，并构造新Product实例
        if (productDAO.getProductByName(newProductInfo.getName()) != null) throw new IllegalException("商品名", newProductInfo.getName(), "已存在");
        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();
        String[] imgUrls = ImgUtil.MultiBase64StringsToLocalImg(imgBase64Strings, imgTypes);
        Product newProduct = new Product(-1, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                newProductInfo.getStock(), newProductInfo.getSaleCount(), imgUrls, categorySecond.getId());

        //尝试新增商品，需要对ProductName对应加锁
        Statistic.productNameLock.putIfAbsent(newProduct.getName(), new Object());
        synchronized (Statistic.productNameLock.get(newProduct.getName())) {
            if (productDAO.getProductByName(newProductInfo.getName()) != null)
                throw new IllegalException("商品名", newProductInfo.getName(), "已存在");
            productDAO.addProduct(newProduct);
            categorySecondDAO.addProductCount(categorySecond.getId());
            categoryFirstDAO.addProductCount(categorySecond.getCategoryFirstId());
        }
        Statistic.productNameLock.remove(newProduct.getName());
    }

    /**
     * 根据新增商品所需信息(newProductInfo)来新增商品，商品名不能重复
     * @param newProductInfo 新增商品所需信息
     * @param productId 要修改的商品Id
     * @throws IllegalException 信息不合法
     * @throws IOException 上传图片IOException
     */
    @Transactional(propagation = Propagation.REQUIRED) //多个更新操作，需要事务
    public void modifyProduct(NewProductInfo newProductInfo, int productId) throws IOException, IllegalException {
        ProductUtil.checkNewProductInfoLegality(newProductInfo);

        //获取新商品的二级分类
        CategorySecond categorySecond = categorySecondDAO.getCategorySecondByName(newProductInfo.getCategorySecondName());
        if (categorySecond == null) throw new IllegalException("二级分类", newProductInfo.getCategorySecondName(), "不存在");

        //根据productId来获取原来的Product
        Product originProduct = productDAO.getProductById(productId);
        if (originProduct == null) throw new IllegalException("商品Id", productId + "", "不存在");

        //获取原来商品的二级分类
        CategorySecond originCategorySecond = categorySecondDAO.getCategorySecondById(originProduct.getCategorySecondId());
        if (originCategorySecond == null) throw new IllegalException("二级分类Id", originProduct.getCategorySecondId() + "", "不存在");

        //如果商品名不存在，则上传多张图片，并构造新Product实例
        if (productDAO.getProductByName(newProductInfo.getName()) != null) throw new IllegalException("商品名", newProductInfo.getName(), "已存在");
        String[] imgBase64Strings = newProductInfo.getImgBase64Strings();
        String[] imgTypes = newProductInfo.getImgTypes();
        String[] imgUrls = ImgUtil.MultiBase64StringsToLocalImg(imgBase64Strings, imgTypes);
        Product newProduct = new Product(-1, newProductInfo.getName(), newProductInfo.getSubTitle(), newProductInfo.getPrice(),
                newProductInfo.getStock(), newProductInfo.getSaleCount(), imgUrls, categorySecond.getId());

        //尝试更新商品，需要对ProductName对应加锁
        Statistic.productNameLock.putIfAbsent(newProduct.getName(), new Object());
        synchronized (Statistic.productNameLock.get(newProduct.getName())) {
            if (productDAO.getProductByName(newProductInfo.getName()) != null)
                throw new IllegalException("商品名", newProductInfo.getName(), "已存在");
            newProduct.setId(productId);
            productDAO.updateProductById(newProduct);
            //减少原来分类的商品数
            categorySecondDAO.reduceProductCount(originCategorySecond.getId());
            categoryFirstDAO.reduceProductCount(originCategorySecond.getCategoryFirstId());
            //增加现在分类的商品数
            categorySecondDAO.addProductCount(categorySecond.getId());
            categoryFirstDAO.addProductCount(categorySecond.getCategoryFirstId());
        }
        //后续不需要对此ProductName的锁，因为后续访问都会得到该商品名已存在数据库
        Statistic.productNameLock.remove(newProduct.getName());
    }

    /**
     * 根据商品名删除商品
     * @param productName 商品名
     * @throws IllegalException 信息不合法
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteProduct(String productName) throws IllegalException {
        //对ProductName对应的ReentrantLock加锁，防止重复删除商品
        Statistic.productNameLock.putIfAbsent(productName, new Object());
        synchronized (Statistic.productNameLock.get(productName)) {
            //查找要删除的商品和对应分类
            Product product = productDAO.getProductByName(productName);
            if (product == null) throw new IllegalException("商品名", productName, "不存在");
            CategorySecond categorySecond = categorySecondDAO.getCategorySecondById(product.getCategorySecondId());
            //删除商品
            productDAO.deleteProductByName(productName);
            //减少商品分类的商品数
            categorySecondDAO.reduceProductCount(categorySecond.getId());
            categoryFirstDAO.reduceProductCount(categorySecond.getCategoryFirstId());
        }
        Statistic.productNameLock.remove(productName);
    }

    public Product getProductByProductId(int productId) {
        return productDAO.getProductById(productId);
    }
}
