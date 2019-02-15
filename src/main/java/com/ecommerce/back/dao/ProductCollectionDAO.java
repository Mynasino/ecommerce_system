package com.ecommerce.back.dao;

import com.ecommerce.back.model.ProductCollection;

import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface ProductCollectionDAO {
    String TABLE_NAME = "product_collection";
    String SELECT_FIELDS = "user_id,product_id";
    String INSERT_FIELDS_DB = "user_id,product_id";
    String INSERT_FIELDS = "#{userId},#{productId}";

    @Select(value = {"SELECT product_id FROM ", TABLE_NAME, " WHERE user_id = #{userId}"})
    List<Integer> getProductIdsByUserId(int userId);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE user_id = #{userId} AND product_id = #{productId}"})
    void deleteProductCollectionByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addProductCollection(ProductCollection productCollection) throws DataAccessException;
}
