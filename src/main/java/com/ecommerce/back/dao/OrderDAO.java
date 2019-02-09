package com.ecommerce.back.dao;

import com.ecommerce.back.model.Order;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderDAO {
    String TABLE_NAME = "order";
    String SELECT_FIELDS = "id,address,customer_name,mobile,create_time,pay_time,delivery_time,user_id,status_code,category_second_id";
    String INSERT_FIELDS_DB = "address,customer_name,mobile,create_time,pay_time,delivery_time,user_id,status_code,category_second_id";
    String INSERT_FIELDS = "#{address},#{customerName},#{mobile},#{createTime},#{payTime},#{deliveryTime},#{userId},#{statusCode},#{categorySecondId}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    Order getOrderById(int id);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addOrder(Order order);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteOrderById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
