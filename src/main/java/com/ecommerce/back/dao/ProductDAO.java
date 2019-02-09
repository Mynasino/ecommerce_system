package com.ecommerce.back.dao;

import com.ecommerce.back.model.Product;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    int addProduct(Product product);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE name = #{name}"})
    int deleteProductByName(String name);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET ", UPDATE_FIELDS ," WHERE id = #{id}"})
    int updateProduct(Product product);
}
