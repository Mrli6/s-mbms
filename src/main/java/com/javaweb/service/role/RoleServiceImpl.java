package com.kuang.service.role;

import com.kuang.dao.BaseDao;
import com.kuang.dao.role.RoleDao;
import com.kuang.dao.role.RoleDaoImpl;
import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDaoImpl;
    public RoleServiceImpl() {
        roleDaoImpl = new RoleDaoImpl();
    }

    // 获取角色列表
    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;

        try {
            connection = BaseDao.getConnection();
            roleList = roleDaoImpl.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return roleList;
    }

}
