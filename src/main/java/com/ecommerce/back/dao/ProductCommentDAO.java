package com.ecommerce.back.dao;

import com.ecommerce.back.model.ProductComment;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductCommentDAO {
    String TABLE_NAME = "product_comment";
    String SELECT_FIELDS = "id,product_id,user_id,content,img_urls,score,created_time,order_id";
    String INSERT_FIELDS_DB = "product_id,user_id,content,img_urls,score,created_time,order_id";
    String INSERT_FIELDS = "#{productId},#{userId},#{content},#{imgUrls},#{score},#{createdTime},#{orderId}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    ProductComment getProductCommentById(int id);

    @Select(value = {"SELECT id FROM ", TABLE_NAME, " WHERE order_id = #{orderId} AND product_id = #{productId}"})
    Integer getProductCommentIdByOrderIdAndProductId(@Param("orderId") int orderId, @Param("productId") int productId);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE order_id = #{orderId} AND product_id = #{productId}"})
    ProductComment getProductCommentIdByOrderAndProductId(@Param("orderId") int orderId, @Param("productId") int productId);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE user_id = #{userId}"})
    List<ProductComment> getProductCommentByUserId(int userId);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE product_id = #{productId} LIMIT #{limit} OFFSET #{offset}"})
    List<ProductComment> getProductCommentByProductId(@Param("productId") int productId,
                                                      @Param("limit") int limit,
                                                      @Param("offset") int offset);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addProductComment(ProductComment productComment);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteProductCommentById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
