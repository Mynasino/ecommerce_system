package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.NewOrderComment;
import com.ecommerce.back.jsonInfo.NewProductComment;

public class CommentUtil {
    /**
     * 检查创建新的订单评论的信息是否合法，若不合法则抛出异常
     * @param newOrderComment 创建新的订单评论所需的信息
     */
    public static void validateNewOrderComment(NewOrderComment newOrderComment) throws IllegalException {
        int scoreLogistics = newOrderComment.getScoreLogistics();
        int scoreQuality = newOrderComment.getScoreQuality();
        int scoreService = newOrderComment.getScoreService();

        if (!(scoreLogistics >= 0 && scoreLogistics <= 5))
            throw new IllegalException("物流评分", scoreLogistics + "", "应在范围[0,5]内");
        if (!(scoreQuality >= 0 && scoreQuality <= 5))
            throw new IllegalException("质量评分", scoreQuality + "", "应在范围[0,5]内");
        if (!(scoreService >= 0 && scoreService <= 5))
            throw new IllegalException("服务评分", scoreService + "", "应在范围[0,5]内");
    }
    /**
     * 检查创建新的商品评论的信息是否合法，若不合法则抛出异常
     * @param newProductComment 创建新的商品评论所需的信息
     */
    public static void validateNewProductComment(NewProductComment newProductComment) throws IllegalException  {
        int score = newProductComment.getScore();

        if (!(score >= 0 && score <= 5))
            throw new IllegalException("商品评分", score + "", "应在范围[0,5]内");
    }
}
