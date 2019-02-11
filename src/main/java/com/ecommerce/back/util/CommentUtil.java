package com.ecommerce.back.util;

import com.ecommerce.back.jsonInfo.NewOrderComment;
import com.ecommerce.back.jsonInfo.NewProductComment;

public class CommentUtil {
    public static void validateNewOrderComment(NewOrderComment newOrderComment) {
        int scoreLogistics = newOrderComment.getScoreLogistics();
        int scoreQuality = newOrderComment.getScoreQuality();
        int scoreService = newOrderComment.getScoreService();

        if (!(scoreLogistics >= 0 && scoreLogistics <= 5))
            throw new IllegalStateException("scoreLogistics must in range [0,5]");
        if (!(scoreQuality >= 0 && scoreQuality <= 5))
            throw new IllegalStateException("scoreQuality must in range [0,5]");
        if (!(scoreService >= 0 && scoreService <= 5))
            throw new IllegalStateException("scoreService must in range [0,5]");
    }
    public static void validateNewProductComment(NewProductComment newProductComment) {
        int score = newProductComment.getScore();

        if (!(score >= 0 && score <= 5))
            throw new IllegalStateException("score must in range [0,5]");
    }
}
