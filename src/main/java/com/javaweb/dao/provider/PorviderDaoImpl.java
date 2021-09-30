package com.kuang.dao.provider;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Provider;
import com.mysql.cj.util.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PorviderDaoImpl implements ProviderDao {

    @Override
    public List<Provider> getProviderList(Connection connection, String queryProCode, String queryProName) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Provider> res = new LinkedList<>();

        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select * from smbms_provider where 1=1 ");
            List<Object> args = new ArrayList<>();

            if(!StringUtils.isNullOrEmpty(queryProCode)){
                sql.append("and proCode like ? ");
                args.add("%"+queryProCode+"%");
            }
            if(!StringUtils.isNullOrEmpty(queryProName)){
                sql.append("and proName like ?");
                args.add("%"+queryProName+"%");
            }

            rs = BaseDao.excute(connection, ps, sql.toString(), args.toArray(), rs);

            while (rs.next()) {
                Provider temp = new Provider();
                temp.setId(rs.getInt("id"));
                temp.setProCode(rs.getString("proCode"));
                temp.setProName(rs.getString("proName"));
                temp.setProContact(rs.getString("proContact"));
                temp.setProPhone(rs.getString("proPhone"));
                temp.setProFax(rs.getString("proFax"));
                temp.setCreationDate(rs.getDate("creationDate"));
                res.add(temp);
            }

            BaseDao.destory(null, ps, rs);
        }

        return res;
    }

    @Override
    public Provider getProviderById(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Provider provider = new Provider();

        if(connection != null){
            String sql = "select * from smbms_provider where id = ?";
            Object[] args = {id};

            rs = BaseDao.excute(connection, ps, sql, args, rs);
            if(rs.next()){
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreatedBy(rs.getInt("createdBy"));
                provider.setCreationDate(rs.getDate("creationDate"));
                provider.setModifyDate(rs.getDate("modifyDate"));
                provider.setModifyBy(rs.getInt("modifyBy"));
            }
            BaseDao.destory(null, ps, rs);
        }

        return provider;
    }

    @Override
    public boolean addProvide(Connection connection, Provider provider) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("insert into smbms_provider(proCode,proName,proDesc,proContact,proPhone," +
                    "proAddress,proFax,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)");
            Object[] args = { provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate()
            };
            count = BaseDao.excute(connection, ps, sql.toString(), args);
            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    @Override
    public boolean modifyProvider(Connection connection, Provider provider) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "update smbms_provider set proCode=? , proName=? , proDesc=?" +
                ", proContact=? , proPhone=? , proAddress=? , proFax=? , modifyBy=?" +
                ", modifyDate=? where id=?";
            Object[] args = { provider.getProCode() , provider.getProName() ,
                    provider.getProDesc() , provider.getProContact() , provider.getProPhone() ,
                    provider.getProAddress() , provider.getProFax() , provider.getModifyBy() ,
                    provider.getModifyDate() , provider.getId()
            };
            count = BaseDao.excute(connection, ps, sql, args);
            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

    @Override
    public boolean deleteProvider(Connection connection, int id) throws SQLException {
        PreparedStatement ps = null;
        int count = 0;

        if(connection != null){
            String sql = "delete from smbms_provider where id=?";
            Object[] args = {id};
            count = BaseDao.excute(connection, ps, sql, args);
            BaseDao.destory(null, ps, null);
        }

        return count == 1 ? true : false;
    }

}
