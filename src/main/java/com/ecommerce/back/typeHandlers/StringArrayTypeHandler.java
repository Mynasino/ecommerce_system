package com.ecommerce.back.typeHandlers;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用于将实体类的String数组映射为数据库的VARCHAR/TEXT类
 * 具体实现为将String数组的JSON格式串存入数据库
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StringArrayTypeHandler extends BaseTypeHandler<String[]> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (str == null) return null;
        List<String> imgUrls = JSON.parseArray(str, String.class);
        return imgUrls.toArray(new String[0]);
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (str == null) return null;
        List<String> imgUrls = JSON.parseArray(str, String.class);
        return (String[])imgUrls.toArray();
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (str == null) return null;
        List<String> imgUrls = JSON.parseArray(str, String.class);
        return (String[])imgUrls.toArray();
    }
}
