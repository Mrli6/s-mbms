package com.kuang.service.user;

import com.kuang.dao.BaseDao;
import com.kuang.dao.user.UserDao;
import com.kuang.dao.user.UserDaoImpl;
import com.kuang.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDaoImpl;
    public UserServiceImpl(){
        userDaoImpl = new UserDaoImpl();
    }

    @Override
        public User login(String userCode, String password) {

            Connection connection = null;
            User user = null;

            try {
                connection = BaseDao.getConnection();
                user = userDaoImpl.getLoginUser(connection, userCode);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.destory(connection, null, null);
            }


            return user;
    }

    // 根据id改密码
    @Override
    public boolean updatePassword(int id, String password) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            if( userDaoImpl.updatePassword(connection ,id, password) > 0 ){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.destory(connection, null, null);
        }

        return flag;
    }

    @Override
    public int getUserCount(String userName, int id) {
        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = userDaoImpl.getUserCount(connection, userName, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return count;
    }

    @Override
    public List<User> getUserList(String userName, int id, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> list = new LinkedList<User>();

        try {
            connection = BaseDao.getConnection();
            list = userDaoImpl.getUserList(connection, userName, id, currentPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return list;
    }

    @Override
    public boolean addUser(User user) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            // 开jdbc事务
            connection.setAutoCommit(false);
            flag = userDaoImpl.addUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            BaseDao.destory(connection, null, null);
        }

        return flag;
    }

    @Override
    public boolean userIsExist(String userCode) {
        Connection connection = null;
        boolean  flag = false;

        try {
            connection = BaseDao.getConnection();
            User user = userDaoImpl.getLoginUser(connection, userCode);
            if(user != null){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return flag;
    }

    @Override
    public boolean delUserById(int id) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = userDaoImpl.delUser(connection, id);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return flag;
    }

    @Override
    public User getUserById(int id) {
        Connection connection = null;
        User user = new User();

        try {
            connection = BaseDao.getConnection();
            user = userDaoImpl.getUserById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return user;
    }

    @Override
    public boolean modifyUser(User user){
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = userDaoImpl.modifyUser(connection, user);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return flag;
    }

}
