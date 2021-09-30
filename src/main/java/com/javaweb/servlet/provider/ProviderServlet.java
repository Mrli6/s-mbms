package com.kuang.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.kuang.pojo.Provider;
import com.kuang.pojo.User;
import com.kuang.service.provider.ProviderService;
import com.kuang.service.provider.ProviderServiceImpl;
import com.kuang.util.Constant;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if("query".equals(method)){
            this.getProviderList(req, resp);
        } else if ("view".equals(method)) {
            this.getProviderById(req, resp, "providerview.jsp");
        } else if("modify".equals(method)){
            this.getProviderById(req, resp, "providermodify.jsp");
        } else if("modifyprovider".equals(method)){
            this.modifyProvider(req, resp);
        } else if("add".equals(method)){
            this.addProvider(req, resp);
        } else if("delprovider".equals(method)){
            this.deleteProvider(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void getProviderList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");


        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(queryProCode, queryProName);
        req.setAttribute("providerList", providerList);

        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }

    private void getProviderById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException{
        String proid = req.getParameter("proid");

        if(!StringUtils.isNullOrEmpty(proid)){
            int id = Integer.parseInt(proid);

            ProviderService providerService = new ProviderServiceImpl();
            Provider provider = providerService.getProviderById(id);

            req.setAttribute("provider", provider);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void modifyProvider(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        if(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resp.sendRedirect(req.getContextPath()+"/jsp/nolimits.jsp");
            return;
        }

        String id = req.getParameter("proid");
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proDesc = req.getParameter("proDesc");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");

        Provider provider = new Provider();

        provider.setId(Integer.parseInt(id));
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProDesc(proDesc);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setModifyBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setModifyDate(new Date());

        ProviderService providerService = new ProviderServiceImpl();
        if(providerService.modifyProvider(provider)){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("/jsp/providermodify.jsp").forward(req, resp);
        }
    }

    private void addProvider(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();

        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        ProviderService providerService = new ProviderServiceImpl();
        if (providerService.addProvider(provider)) {
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("/jsp/provideradd.jsp").forward(req, resp);
        }
    }

    private void deleteProvider(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String proid = req.getParameter("proid");
        int id = 0;

        if(!StringUtils.isNullOrEmpty(proid)){
            id = Integer.parseInt(proid);
        }

        Map<String, String> resultMap = new HashMap<>();
        if(id <= 0 || ((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resultMap.put("delResult", "notexist");
        }else{
            ProviderService providerService = new ProviderServiceImpl();
            int i = providerService.deleteProvider(id);
            if(i == 0){
                resultMap.put("delResult", "true");
            }else if(i == -1){
                resultMap.put("delResult", "false");
            }else{
                resultMap.put("delResult", String.valueOf(i));
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
