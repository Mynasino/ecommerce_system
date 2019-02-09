package com.ecommerce.back.dao;

import com.ecommerce.back.model.ProductComment;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ProductCommentDAO {
    String TABLE_NAME = "product_comment";
    String SELECT_FIELDS = "id,product_id,user_id,content,img_urls,score,created_time";
    String INSERT_FIELDS_DB = "product_id,user_id,content,img_urls,score,created_time";
    String INSERT_FIELDS = "#{productId},#{userId},#{content},#{imgUrls},#{score},#{createdTime}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    ProductComment getProductCommentById(int id);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addProductComment(ProductComment productComment);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteProductCommentById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
