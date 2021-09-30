package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.mysql.cj.util.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        User user = null;

        if(connection != null){
            String sql = "SELECT * FROM smbms_user WHERE userCode=?";
            Object[] args = {userCode};
            rs = BaseDao.excute(connection, preparedStatement, sql, args, rs);

            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }

            BaseDao.destory(null, preparedStatement, rs);
        }


        return user;
    }

    // 根据id值修改密码
    @Override
    public int updatePassword(Connection connection, int id, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        int count = 0;

        if(connection != null){
            String sql = "UPDATE smbms_user SET userPassword=? WHERE id=?";
            Object[] args = {password, id};
            count = BaseDao.excute(connection, preparedStatement, sql, args);
            BaseDao.destory(null, preparedStatement, null);
        }

        return count;
    }

    // 根据用户名字或角色id查询数据
    @Override
    public int getUserCount(Connection connection, String username, int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int count = 0;

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            List<Object> list = new LinkedList<Object>();
            sql.append("SELECT COUNT(*) AS count from smbms_user u, smbms_role r WHERE u.userRole =  r.id");

            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" AND u.userName LIKE ?");
                list.add("'%"+username+"%'");
            }
            if(id > 0){
                sql.append(" AND r.id = ?");
                list.add(id);
            }
            Object[] args = list.toArray();

            rs = BaseDao.excute(connection, preparedStatement, sql.toString(), args, rs);
            if(rs.next()){
                count = rs.getInt("count");
            }

            BaseDao.destory(null, preparedStatement, rs);

        }

        return count;
    }

    // 根据用户名字或角色id查询详细数据
    @Override
    public List<User> getUserList(Connection connection, String userName, int id, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<User> res = new LinkedList<User>();

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            List<Object> list = new LinkedList<Object>();
            sql.append("SELECT u.*, r.roleName AS userRoleName from smbms_user u, smbms_role r WHERE u.userRole =  r.id");

            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" AND u.userName LIKE ?");
                list.add("%"+userName+"%");
            }
            if(id > 0){
                sql.append(" AND r.id = ?");
                list.add(id);
            }

            sql.append(" ORDER BY creationDate DESC limit ?,?");
            int start = (currentPageNo-1)*pageSize;
            list.add(start);
            list.add(pageSize);

            Object[] args = list.toArray();

            rs = BaseDao.excute(connection, preparedStatement, sql.toString(), args, rs);

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                res.add(user);
            }

            BaseDao.destory(null, preparedStatement, rs);
        }

        return res;
    }

    // 添加用户
    @Override
    public boolean addUser(Connection connection, User user) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "insert into smbms_user(userCode, userName, userPassword, gender," +
                    "birthday, phone, address, userRole, createdBy, creationDate, modifyBy, modifyDate)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?)";

            Object[] args = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getGender(), user.getBirthday(), user.getPhone(), user.getAddress(),
                    user.getUserRole(), user.getCreatedBy(), user.getCreationDate(), user.getModifyBy(),
                    user.getModifyDate()};
            count = BaseDao.excute(connection, ps, sql, args);

            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    // 删除用户
    @Override
    public boolean delUser(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        int res = 0;

        if(connection != null){
            String sql = "delete from smbms_user where id = ?";
            Object[] args = {id};
            res = BaseDao.excute(connection, ps, sql, args);
            BaseDao.destory(null, ps, null);
        }

        return res == 1 ? true : false;
    }

    // 根据id查询某一用户数据
    @Override
    public User getUserById(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = new User();

        if(connection != null){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] args = {id};
            rs = BaseDao.excute(connection, ps, sql, args, rs);
            if(rs.next()){
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.destory(null, ps, rs);
        }

        return user;
    }

    @Override
    public boolean modifyUser(Connection connection, User user) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "update smbms_user set userName=?,gender=?,birthday=?,"+
                    "phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] args = {user.getUserName(), user.getGender(), user.getBirthday(),
                        user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                        user.getModifyDate(), user.getId()};
            count = BaseDao.excute(connection, ps, sql, args);
            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    @Test
    public void test(){
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            User user = userDao.getUserById(BaseDao.getConnection(), 20);
            System.out.println(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
