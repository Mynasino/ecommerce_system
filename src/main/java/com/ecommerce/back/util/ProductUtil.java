package com.ecommerce.back.util;

import com.ecommerce.back.jsonInfo.NewProductInfo;

public class ProductUtil {
    public static boolean isLegalNewProductInfo(NewProductInfo newProductInfo) {
        return (!"".equals(newProductInfo.getName()))
                && newProductInfo.getPrice() > 0
                && newProductInfo.getStock() >= 0
                && newProductInfo.getSaleCount() >= 0
                && newProductInfo.getImgBase64Strings().length != 0
                && newProductInfo.getImgBase64Strings().length == newProductInfo.getImgTypes().length
                && (!"".equals(newProductInfo.getCategorySecondName()));
    }
}
