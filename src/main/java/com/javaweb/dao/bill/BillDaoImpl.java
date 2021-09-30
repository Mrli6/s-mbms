package com.kuang.dao.bill;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Bill;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BillDaoImpl implements BillDao {
    @Override
    public List<Bill> getBillList(Connection connection, String queryProductName, int queryProviderId, int queryIsPayment) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Bill> res = new ArrayList<>();

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select b.*, p.proName as providerName from smbms_provider p, smbms_bill b where b.providerId = p.id");
            List<Object> args = new LinkedList<>();

            if(!StringUtils.isNullOrEmpty(queryProductName)){
                sql.append(" and b.productName like ?");
                args.add("%"+queryProductName+"%");
            }
            if(queryProviderId > 0){
                sql.append(" and b.providerId=?");
                args.add(queryProviderId);
            }
            if(queryIsPayment == 1 || queryIsPayment == 2){
                sql.append(" and b.isPayment=?");
                args.add(queryIsPayment);
            }

            rs = BaseDao.excute(connection, ps, sql.toString(), args.toArray(), rs);

            while(rs.next()){
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setCreatedBy(rs.getInt("createdBy"));
                bill.setCreationDate(rs.getDate("creationDate"));
                bill.setModifyBy(rs.getInt("modifyBy"));
                bill.setModifyDate(rs.getDate("modifyDate"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("providerName"));
                res.add(bill);
            }

            BaseDao.destory(null, ps, rs);
        }

        return res;
    }

    @Override
    public Bill getBillById(Connection connection, int id) throws SQLException    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Bill res = new Bill();

        if(connection != null){
            String sql = "select b.*, p.proName as providerName from smbms_provider p, smbms_bill b where b.providerId = p.id and b.id = ?";
            Object[] args = {id};
            rs = BaseDao.excute(connection, ps, sql, args, rs);

            if(rs.next()){
                res.setId(rs.getInt("id"));
                res.setBillCode(rs.getString("billCode"));
                res.setProductName(rs.getString("productName"));
                res.setProductDesc(rs.getString("productDesc"));
                res.setProductUnit(rs.getString("productUnit"));
                res.setProductCount(rs.getBigDecimal("productCount"));
                res.setTotalPrice(rs.getBigDecimal("totalPrice"));
                res.setIsPayment(rs.getInt("isPayment"));
                res.setCreatedBy(rs.getInt("createdBy"));
                res.setCreationDate(rs.getDate("creationDate"));
                res.setModifyBy(rs.getInt("modifyBy"));
                res.setModifyDate(rs.getDate("modifyDate"));
                res.setProviderId(rs.getInt("providerId"));
                res.setProviderName(rs.getString("providerName"));
            }

            BaseDao.destory(null, ps, rs);
        }

        return res;
    }

    @Override
    public boolean addBill(Connection connection, Bill bill) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "insert into smbms_bill(billCode, productName, productDesc, productUnit," +
                    "productCount, totalPrice, isPayment, createdBy, creationDate, modifyBy, modifyDate," +
                    "providerId) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] args = { bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getCreatedBy(), bill.getCreationDate(), bill.getModifyBy(), bill.getModifyDate(),
                    bill.getProviderId()
            };
            count = BaseDao.excute(connection, ps, sql, args);

            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    @Override
    public boolean modifyBill(Connection connection, Bill bill) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "update smbms_bill set billCode=?, productName=?, productDesc=?," +
                    "productUnit=?, productCount=?, totalPrice=?, isPayment=?,createdBy=?, " +
                    "creationDate=?,modifyBy=?, modifyDate=?, providerId=? " +
                    "where id = ?";
            Object[] args = { bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getCreatedBy(), bill.getCreationDate(), bill.getModifyBy(), bill.getModifyDate(),
                    bill.getProviderId(), bill.getId()
            };
            count = BaseDao.excute(connection, ps, sql, args);

            BaseDao.destory(null, ps, null);
        }


        return count == 1 ? true : false;
    }

    @Override
    public boolean deleteBillById(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "delete from smbms_bill where id = ?";
            Object[] args = {id};
            count = BaseDao.excute(connection, ps, sql, args);

            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    @Override
    public int getBillByProviderId(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        if(connection != null){
            String sql = "select * from smbms_bill where providerId = ?";
            Object[] args = {id};
            rs = BaseDao.excute(connection, ps, sql, args, rs);
            while( rs.next()){
                count += 1;
            }

            BaseDao.destory(null, ps, rs);
        }

        return count;
    }
}
