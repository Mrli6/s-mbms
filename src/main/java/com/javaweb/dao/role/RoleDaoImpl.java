package com.kuang.dao.role;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    // 获取角色列表
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Role> res = new LinkedList<Role>();

        if(connection != null){
            String sql = "SELECT * FROM smbms_role";
            Object[] args = {};
            rs = BaseDao.excute(connection, preparedStatement, sql, args, rs);

            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                res.add(role);
            }

            BaseDao.destory(null, preparedStatement, rs);
        }

        return res;
    }

}
