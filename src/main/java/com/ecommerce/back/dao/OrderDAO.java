package com.ecommerce.back.dao;

import com.ecommerce.back.model.Order;

import com.ecommerce.back.model.OrderStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderDAO {
    String TABLE_NAME = "order_";
    String SELECT_FIELDS = "id,address,mobile,create_time,pay_time,delivery_time,user_id,status_code";
    String INSERT_FIELDS_DB = "address,mobile,create_time,pay_time,delivery_time,user_id,status_code";
    String INSERT_FIELDS = "#{address},#{mobile},#{createTime},#{payTime},#{deliveryTime},#{userId},#{statusCode}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    Order getOrderById(int id);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE user_id = #{userId}"})
    List<Order> getOrdersByUserId(int userId);

    @Select(value = {"SELECT id FROM ", TABLE_NAME, " WHERE user_id = #{userId} AND status_code = " + OrderStatus.SHOPPING_CART})
    Integer getShoppingCartOrderIdByUserId(int userId);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addOrder(Order order);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteOrderById(int id);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET status_code = " + OrderStatus.PENDING_PAYMENT +
            ", address = #{address}, mobile = #{mobile}",
            " WHERE id = #{id}"})
    void updateStatusToPendingPayment(@Param("id") int id, @Param("address") String address, @Param("mobile") String mobile);
}
