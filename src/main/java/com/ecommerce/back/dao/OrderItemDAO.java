package com.ecommerce.back.dao;

import com.ecommerce.back.model.OrderItem;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderItemDAO {
    String TABLE_NAME = "order_item";
    String SELECT_FIELDS = "order_id,product_id,count";
    String INSERT_FIELDS_DB = "order_id,product_id,count";
    String INSERT_FIELDS = "#{orderId},#{productId},#{count}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE order_id = #{orderId} and product_id = #{productId}"})
    OrderItem getOrderItemByOrderIdAndProductId(@Param("orderId") int orderId, @Param("productId") int productId);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE order_id = #{orderId}"})
    List<OrderItem> getOrderItemByOrderId(int orderId);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addOrderItem(OrderItem orderItem);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE order_id = #{orderId} AND product_id = #{productId}"})
    void deleteOrderItemByShoppingCartIdAndProductId(@Param("orderId") int shoppingCartId, @Param("productId") int productId);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET count = #{count} WHERE order_id = #{orderId} AND product_id = #{productId}"})
    void updateOrderItemCountByOrderItem(OrderItem orderItem);
}
