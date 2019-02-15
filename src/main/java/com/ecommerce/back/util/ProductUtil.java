package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.NewProductInfo;

public class ProductUtil {
    public static void checkNewProductInfoLegality(NewProductInfo newProductInfo) throws IllegalException {
        if (newProductInfo.getName() == null || newProductInfo.getName().length() == 0)
            throw new IllegalException("商品名", newProductInfo.getName(), "不能为空");
        if (newProductInfo.getPrice() <= 0)
            throw new IllegalException("商品价格", newProductInfo.getPrice() + "", "必须大于0");
        if (newProductInfo.getStock() <= 0)
            throw new IllegalException("商品库存", newProductInfo.getStock() + "", "必须大于0");
        if (newProductInfo.getSaleCount() <= 0)
            throw new IllegalException("商品销售量", newProductInfo.getSaleCount() + "", "必须大于0");
        if (newProductInfo.getImgBase64Strings().length == 0)
            throw new IllegalException("商品图片Base64编码字符串数组", null, "不能为空");
        if (newProductInfo.getImgBase64Strings().length != newProductInfo.getImgTypes().length)
            throw new IllegalException("商品图片数组和图片类型数组长度",newProductInfo.getImgBase64Strings().length + " " + newProductInfo.getImgTypes().length, "必须一致");
        if (newProductInfo.getCategorySecondName() == null || newProductInfo.getCategorySecondName().length() == 0)
            throw new IllegalException("商品二级分类",newProductInfo.getCategorySecondName(),"不能为空");
    }
}
