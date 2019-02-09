package com.ecommerce.back.dao;

import com.ecommerce.back.model.OrderItem;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderItemDAO {
    String TABLE_NAME = "order_item";
    String SELECT_FIELDS = "id,order_id,product_id,count";
    String INSERT_FIELDS_DB = "order_id,product_id,count";
    String INSERT_FIELDS = "#{orderId},#{productId},#{count}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    OrderItem getOrderItemById(int id);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addOrderItem(OrderItem orderItem);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteOrderItemById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
