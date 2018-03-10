package com.qchery.city.crawler.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecutiveLevelTypeHandler extends BaseTypeHandler<ExecutiveLevel> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ExecutiveLevel parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getLevel());
    }

    @Override
    public ExecutiveLevel getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (rs.wasNull()) {
            return null;
        } else {
            int rsInt = rs.getInt(columnName);
            return ExecutiveLevel.parse(rsInt);
        }
    }

    @Override
    public ExecutiveLevel getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.wasNull()) {
            return null;
        } else {
            int rsInt = rs.getInt(columnIndex);
            return ExecutiveLevel.parse(rsInt);
        }
    }

    @Override
    public ExecutiveLevel getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.wasNull()) {
            return null;
        } else {
            int csInt = cs.getInt(columnIndex);
            return ExecutiveLevel.parse(csInt);
        }
    }
}
