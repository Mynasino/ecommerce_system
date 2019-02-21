package com.ecommerce.back.dao;

import com.ecommerce.back.model.User;

import org.apache.ibatis.annotations.*;
import org.springframework.lang.Nullable;
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

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " LIMIT #{limit} OFFSET #{offset}"})
    List<User> getUsersByLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);

    @Select(value = {"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE user_name = #{userName}"})
    User getUserByUserName(String userName);

    @Select(value = {"SELECT id FROM ", TABLE_NAME, " WHERE user_name = #{userName}"})
    Integer getUserIdByUserName(String userName);

    @Insert(value = {"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS_DB, ") ",
            "VALUES(", INSERT_FIELDS, ")"})
    void addUser(User user);

    @Delete(value = {"DELETE FROM ", TABLE_NAME, " WHERE user_name = #{userName}"})
    void deleteUserByUserName(String userName);

    @Update(value = {"UPDATE ", TABLE_NAME, " SET password = #{password} WHERE user_name = #{userName}"})
    void updateUserPassword(User user);

    /**
     * 绑定SQL在UserDAOMapper.xml中，允许password和imgUrl为null，表示不更新
     */
    void updateUser(@Param("userId") int userId,
                    @Param("userName") String userName,
                    @Param("password") @Nullable String password,
                    @Param("salt") @Nullable String salt,
                    @Param("imgUrl") @Nullable String imgUrl,
                    @Param("mail") String mail);
}
