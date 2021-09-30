package com.kuang.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.kuang.dao.bill.BillDaoImpl;
import com.kuang.pojo.Bill;
import com.kuang.pojo.Provider;
import com.kuang.pojo.User;
import com.kuang.service.bill.BillService;
import com.kuang.service.bill.BillServiceImpl;
import com.kuang.service.provider.ProviderServiceImpl;
import com.kuang.util.Constant;
import com.mysql.cj.util.StringUtils;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if("query".equals(method)){
            this.getBillList(req, resp);
        }else if("view".equals(method)){
            this.getBillById(req, resp, "billview.jsp");
        }else if("modify".equals(method)){
            this.getBillById(req, resp,"billmodify.jsp");
        }else if("modifysave".equals(method)){
            this.modifyBill(req, resp);
        }else if("getproviderlist".equals(method)){
            this.getproviderlist(req, resp);
        }else if("add".equals(method)){
            this.addBill(req, resp);
        }else if("delbill".equals(method)){
            this.deleteBill(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void getBillList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList("","");
        req.setAttribute("providerList", providerList);

        String queryProductName = req.getParameter("queryProductName");
        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }

        String queryProviderId = req.getParameter("queryProviderId");
        int id = 0;
        if(!StringUtils.isNullOrEmpty(queryProviderId)){
            id = Integer.parseInt(queryProviderId);
        }

        String queryIsPayment = req.getParameter("queryIsPayment");
        int is = 0;
        if(!StringUtils.isNullOrEmpty(queryIsPayment)){
            is = Integer.parseInt(queryIsPayment);
        }

        BillServiceImpl billService = new BillServiceImpl();
        List<Bill> billList = billService.getBillList(queryProductName, id, is);
        req.setAttribute("billList", billList);

        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);

    }

    private void getBillById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException{
        String billid = req.getParameter("billid");

        if(!StringUtils.isNullOrEmpty(billid)){
            int id = Integer.parseInt(billid);
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = billService.getBillById(id);

            req.setAttribute("bill", bill);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void getproviderlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<Provider> providerList = new LinkedList<>();

        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("", "");

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(providerList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addBill(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount));
        bill.setTotalPrice(new BigDecimal(totalPrice));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        BillServiceImpl billService = new BillServiceImpl();
        if (billService.addBill(bill)) {
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }
    }

    private void modifyBill(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        if(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resp.sendRedirect(req.getContextPath()+"/jsp/nolimits.jsp");
            return;
        }

        String billId = req.getParameter("id");
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.parseInt(billId));
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount));
        bill.setTotalPrice(new BigDecimal(totalPrice));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        BillServiceImpl billService = new BillServiceImpl();
        if(billService.modifyBill(bill)){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }

    private void deleteBill(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String billId = req.getParameter("billid");
        int id = 0;
        if(!StringUtils.isNullOrEmpty(billId)){
            id = Integer.parseInt(billId);
        }

        Map<String, String> resultMap = new HashMap<>();
        if(id <= 0 || ((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resultMap.put("delResult", "notexist");
        }else{
            BillServiceImpl billService = new BillServiceImpl();
            if (billService.deleteBillById(id)) {
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
