package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.NewProductInfo;

public class ProductUtil {
    /**
     * 检查新增商品所需信息是否合法，若不合法则抛出异常
     * @param newProductInfo 新增商品所需的信息
     * @throws IllegalException 新增商品所需信息不合法
     */
    public static void checkNewProductInfoLegality(NewProductInfo newProductInfo) throws IllegalException {
        if (newProductInfo.getName() == null || newProductInfo.getName().length() == 0)
            throw new IllegalException("商品名", newProductInfo.getName(), "不能为空");
        if (newProductInfo.getPrice() <= 0)
            throw new IllegalException("商品价格", newProductInfo.getPrice() + "", "必须大于0");
        if (newProductInfo.getStock() <= 0)
            throw new IllegalException("商品库存", newProductInfo.getStock() + "", "必须大于0");
        if (newProductInfo.getSaleCount() <= 0)
            throw new IllegalException("商品销售量", newProductInfo.getSaleCount() + "", "必须大于0");
        if (newProductInfo.getImgBase64Strings() == null || newProductInfo.getImgBase64Strings().length == 0)
            throw new IllegalException("商品图片Base64编码字符串数组", null, "不能为空");
        if (newProductInfo.getImgBase64Strings().length != newProductInfo.getImgTypes().length)
            throw new IllegalException("商品图片数组和图片类型数组长度",newProductInfo.getImgBase64Strings().length + " " + newProductInfo.getImgTypes().length, "必须一致");
        if (newProductInfo.getCategorySecondName() == null || newProductInfo.getCategorySecondName().length() == 0)
            throw new IllegalException("商品二级分类",newProductInfo.getCategorySecondName(),"不能为空");
    }

    /**
     * 检查更新商品所需信息是否合法，若不合法则抛出异常
     * @param newProductInfo 更新商品所需的信息
     * @throws IllegalException 更新商品所需信息不合法
     */
    public static void checkUpdateProductInfoLegality(NewProductInfo newProductInfo) throws IllegalException {
        if (newProductInfo.getName() == null || newProductInfo.getName().length() == 0)
            throw new IllegalException("商品名", newProductInfo.getName(), "不能为空");
        if (newProductInfo.getPrice() <= 0)
            throw new IllegalException("商品价格", newProductInfo.getPrice() + "", "必须大于0");
        if (newProductInfo.getStock() <= 0)
            throw new IllegalException("商品库存", newProductInfo.getStock() + "", "必须大于0");
        if (newProductInfo.getSaleCount() <= 0)
            throw new IllegalException("商品销售量", newProductInfo.getSaleCount() + "", "必须大于0");
        //更新时可以不用更新图片
        if (newProductInfo.getImgBase64Strings() != null) {
            if (newProductInfo.getImgBase64Strings().length != newProductInfo.getImgTypes().length)
                throw new IllegalException("商品图片数组和图片类型数组长度", newProductInfo.getImgBase64Strings().length + " " + newProductInfo.getImgTypes().length, "必须一致");
        }
        if (newProductInfo.getCategorySecondName() == null || newProductInfo.getCategorySecondName().length() == 0)
            throw new IllegalException("商品二级分类",newProductInfo.getCategorySecondName(),"不能为空");
    }
}
