package com.kuang.service.bill;

import com.kuang.dao.BaseDao;
import com.kuang.dao.bill.BillDao;
import com.kuang.dao.bill.BillDaoImpl;
import com.kuang.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements BillService {

    private BillDao billDao;

    public BillServiceImpl() {
        this.billDao = new BillDaoImpl();
    }

    @Override
    public List<Bill> getBillList(String queryProductName, int queryProviderId, int queryIsPayment) {
        Connection connection = null;
        List<Bill> res = new ArrayList<>();

        try {
            connection = BaseDao.getConnection();
            res = billDao.getBillList(connection, queryProductName, queryProviderId, queryIsPayment);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return res;
    }

    @Override
    public Bill getBillById(int id) {
        Connection connection = null;
        Bill res = new Bill();

        try {
            connection = BaseDao.getConnection();
            res = billDao.getBillById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.destory(connection, null, null);
        }

        return res;
    }

    @Override
    public boolean addBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = billDao.addBill(connection, bill);
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
    public boolean modifyBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = billDao.modifyBill(connection, bill);
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
    public boolean deleteBillById(int id) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            flag = billDao.deleteBillById(connection, id);
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
}
