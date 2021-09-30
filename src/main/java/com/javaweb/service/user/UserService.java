package com.kuang.service.user;

import com.kuang.pojo.User;

import java.util.List;

public interface UserService {
    // 得到当前登录用户的数据库数据
    User login(String userCode, String password);

    // 修改密码
    boolean updatePassword(int id, String password);

    // 根据用户名字和角色id查询
    int getUserCount(String userName, int id);

    // 根据用户名字或角色id查询详细数据
    List<User> getUserList(String userName, int id, int currentPageNo, int pageSize);

    // 添加用户
    boolean addUser(User user);

    // 如果数据库存在userCode，返回true
    boolean userIsExist(String userCode);

    // 根据id删除用户
    boolean delUserById(int id);

    // 根据id查询某一用户
    User getUserById(int id);

    // 修改用户
    boolean modifyUser(User user);
}
