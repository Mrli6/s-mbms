package com.kuang.service.bill;

import com.kuang.pojo.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getBillList(String queryProductName, int queryProviderId, int queryIsPayment);

    Bill getBillById(int id);

    boolean addBill(Bill bill);

    boolean modifyBill(Bill bill);

    boolean deleteBillById(int id);
}
