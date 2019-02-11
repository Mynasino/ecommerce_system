package com.ecommerce.back.dao;

import com.ecommerce.back.model.OrderComment;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderCommentDAO {
    String TABLE_NAME = "order_comment";
    String SELECT_FIELDS = "id,content,img_urls,score_logistics,score_quality,score_service,order_id";
    String INSERT_FIELDS_DB = "content,img_urls,score_logistics,score_quality,score_service,order_id";
    String INSERT_FIELDS = "#{content},#{imgUrls},#{scoreLogistics},#{scoreQuality},#{scoreService},#{orderId}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    OrderComment getOrderCommentById(int id);

    @Select(value = {"SELECT id FROM ", TABLE_NAME, " WHERE order_id = #{orderId}"})
    Integer getOrderCommentIdByOrderId(int orderId);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE order_id = #{orderId}"})
    OrderComment getOrderCommentByOrderId(int orderId);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addOrderComment(OrderComment orderComment);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteOrderCommentById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
