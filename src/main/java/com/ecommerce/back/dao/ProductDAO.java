package com.ecommerce.back.dao;

import com.ecommerce.back.model.Product;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductDAO {
    String TABLE_NAME = "product";
    String SELECT_FIELDS = "id,name,sub_title,price,stock,sale_count,img_urls,category_second_id";
    String INSERT_FIELDS_DB = "name,sub_title,price,stock,sale_count,img_urls,category_second_id";
    String INSERT_FIELDS = "#{name},#{subTitle},#{price},#{stock},#{saleCount},#{imgUrls},#{categorySecondId}";
    String UPDATE_FIELDS = "name = #{name}, sub_title = #{subTitle}, price = #{price}, stock = #{stock}, sale_count = #{saleCount}, img_urls = #{imgUrls}, category_second_id = #{categorySecondId}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    Product getProductById(int id);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE name = #{name}"})
    Product getProductByName(String name);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME,
            " WHERE category_second_id = #{categorySecondId}",
            " LIMIT #{limit} OFFSET #{offset}"})
    List<Product> getProductsByCIdLimitOffset(@Param("categorySecondId") int categorySecondId,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME})
    List<Product> getAllProducts();

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addProduct(Product product);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE name = #{name}"})
    void deleteProductByName(String name);

    /**
     * 见ProductDAOMapper.xml，允许imgUrls为null，表示不更新
     */
    void updateProductById(Product product);
}
