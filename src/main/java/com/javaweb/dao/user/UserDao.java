package com.kuang.dao.user;

import com.kuang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    // 得到当前登录用户在数据库中的数据
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改密码
    int updatePassword(Connection connection, int id, String password) throws SQLException;

    // 根据用户名字或角色id查询数据
    int getUserCount(Connection connection, String username, int id) throws SQLException;

    List<User> getUserList(Connection connection, String userName, int id, int currentPageNo, int pageSize) throws SQLException;

    boolean addUser(Connection connection, User user) throws SQLException;

    boolean delUser(Connection connection, int id) throws SQLException;

    User getUserById(Connection connection, int id) throws SQLException;

    boolean modifyUser(Connection connection, User user) throws SQLException;
}
