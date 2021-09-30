package com.kuang.dao.provider;

import com.kuang.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {

    // 获取所有供应商
    List<Provider> getProviderList(Connection connection, String queryProCode, String queryProName) throws SQLException;

    // 根据供应商id查询数据
    Provider getProviderById(Connection connection, int id) throws SQLException;

    // 添加供应商
    boolean addProvide(Connection connection, Provider provider) throws SQLException;

    // 修改供应商
    boolean modifyProvider(Connection connection, Provider provider) throws SQLException;

    // 删除供应商
    boolean deleteProvider(Connection connection, int id) throws SQLException;
}
