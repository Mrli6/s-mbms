package com.kuang.service.provider;

import com.kuang.dao.BaseDao;
import com.kuang.dao.bill.BillDaoImpl;
import com.kuang.dao.provider.PorviderDaoImpl;
import com.kuang.dao.provider.ProviderDao;
import com.kuang.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {

    ProviderDao providerDao;

    public ProviderServiceImpl(){
        providerDao = new PorviderDaoImpl();
    }

    @Override
    public List<Provider> getProviderList(String queryProCode, String queryProName) {
        Connection connection = null;
        List<Provider> res = new LinkedList<>();

        try {
            connection = BaseDao.getConnection();
            res = providerDao.getProviderList(connection, queryProCode, queryProName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            BaseDao.destory(connection, null, null);
        }

        return res;
    }

    @Override
    public Provider getProviderById(int id){
        Connection connection = null;
        Provider provider = new Provider();

        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.destory(connection, null, null);
        }

        return provider;
    }

    @Override
    public boolean modifyProvider(Provider provider) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = providerDao.modifyProvider(connection, provider);
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
    public boolean addProvider(Provider provider){
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = providerDao.addProvide(connection, provider);
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
    public int deleteProvider(int id) {
        Connection connection = null;
        int count = -1;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            BillDaoImpl billDao = new BillDaoImpl();
            count = billDao.getBillByProviderId(connection, id);
            if(count == 0){
                providerDao.deleteProvider(connection, id);
            }
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

        return count;
    }
}
