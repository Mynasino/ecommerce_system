package com.ecommerce.back.dao;

import com.ecommerce.back.model.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String SELECT_FIELDS = "id,user_name,password,img_url,salt,mail";
    String INSERT_FIELDS_DB = "user_name,password,img_url,salt,mail";
    String INSERT_FIELDS = "#{userName},#{password},#{imgUrl},#{salt},#{mail}";
    String UPDATE_FIELDS = "user_name = #{userName}, password = #{password}, img_url = #{imgUrl}, salt = #{salt}, mail = #{mail}";

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    User getUserById(int id);

    @Select(value = {"SELECT id FROM ", TABLE_NAME})
    List<Integer> getUserIds();

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE user_name = #{userName}"})
    User getUserByUserName(String userName);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addUser(User user);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE user_name = #{userName}"})
    int deleteUserByUserName(String userName);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET password = #{password} WHERE user_name = #{userName}"})
    int updateUserPassword(User user);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET ",UPDATE_FIELDS," WHERE id = #{id}"})
    int updateUser(User user);
}
