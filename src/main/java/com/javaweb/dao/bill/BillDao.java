package com.kuang.dao.bill;

import com.kuang.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    List<Bill> getBillList(Connection connection, String queryProductName, int queryProviderId, int queryIsPayment) throws SQLException;

    Bill getBillById(Connection connection, int id) throws SQLException;

    boolean addBill(Connection connection, Bill bill) throws SQLException;

    boolean modifyBill(Connection connection, Bill bill) throws SQLException;

    boolean deleteBillById(Connection connection, int id) throws SQLException;

    int getBillByProviderId(Connection connection, int id) throws SQLException;
}
