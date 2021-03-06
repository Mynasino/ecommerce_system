package com.ecommerce.back.dao;

import com.ecommerce.back.model.CategorySecond;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CategorySecondDAO {
    String TABLE_NAME = "category_second";
    String SELECT_FIELDS = "id,name,count,category_first_id";
    String INSERT_FIELDS_DB = "name,count,category_first_id";
    String INSERT_FIELDS = "#{name},#{count},#{categoryFirstId}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    CategorySecond getCategorySecondById(int id);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE name = #{name}"})
    CategorySecond getCategorySecondByName(String name);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE category_first_id = #{categoryFirstId}"})
    List<CategorySecond> getCategorySecondsByCategoryFirstId(int categoryFirstId);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addCategorySecond(CategorySecond categorySecond);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE name = #{name}"})
    void deleteCategorySecondByName(String name);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET count = count + 1 WHERE id = #{id}"})
    void addProductCount(int id);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET count = count - 1 WHERE id = #{id}"})
    void reduceProductCount(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
