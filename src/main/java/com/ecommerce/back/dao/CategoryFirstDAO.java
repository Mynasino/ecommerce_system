package com.ecommerce.back.dao;

import com.ecommerce.back.model.CategoryFirst;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CategoryFirstDAO {
    String TABLE_NAME = "category_first";
    String SELECT_FIELDS = "id,name,count";
    String INSERT_FIELDS_DB = "name,count";
    String INSERT_FIELDS = "#{name},#{count}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    CategoryFirst getCategoryFirstById(int id);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addCategoryFirst(CategoryFirst categoryFirst);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE id = #{id}"})
    void deleteCategoryFirstById(int id);

    //@Update(value = {"UPDATE ", TABLE_NAME, " SET status = 0 WHERE ticket = #{ticket}"})
    //void updateLoginTicketStatus(String ticket);
}
