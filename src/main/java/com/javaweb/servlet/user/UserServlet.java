package com.kuang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.role.RoleService;
import com.kuang.service.role.RoleServiceImpl;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constant;
import com.kuang.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 代码复用
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if ("savepwd".equals(method)) {
            this.updatePassword(req, resp);
        } else if ("pwdmodify".equals(method)) {
            this.identityPassword(req, resp);
        } else if ("query".equals(method)) {
            this.userManager(req, resp);
        } else if ("add".equals(method)) {
            this.addUser(req, resp);
        } else if ("ucexist".equals(method)){
            this.ucexist(req, resp);
        } else if("deluser".equals(method)){
            this.delUser(req, resp);
        } else if("view".equals(method)){
            this.getUserById(req, resp, "userview.jsp");
        } else if("modify".equals(method)){
            this.getUserById(req, resp, "usermodify.jsp");
        } else if("modifyexe".equals(method)){
            this.modifyUser(req, resp);
        } else if("getrolelist".equals(method)){
            this.getRoleList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String newPassword = req.getParameter("newpassword");

        if (o != null && !StringUtils.isNullOrEmpty(newPassword)) {
            UserService userService = new UserServiceImpl();
            boolean flag = userService.updatePassword(((User) o).getId(), newPassword);
            if (flag) {
                req.setAttribute(Constant.Message, "修改成功，请重新登录");
                req.getSession().removeAttribute(Constant.USER_SESSION);
            } else {
                req.setAttribute(Constant.Message, "修改失败，请重新修改");
            }
        } else {
            req.setAttribute(Constant.Message, "新密码有问题");
        }

        resp.sendRedirect(req.getContextPath()+"/jsp/pwdmodify.jsp");
    }

    private void identityPassword(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else {
            if (((User) o).getUserPassword().equals(oldpassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
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

    private void userManager(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryUserName = req.getParameter("queryname");

        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        UserService userService = new UserServiceImpl();
        int pageSize = 5;
        int currentPageNo = 1;


        /*if(queryUserName == null){
            queryUserName = "";
        }*/
        if (!StringUtils.isNullOrEmpty(temp)) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();

        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        try {
            User user = new User();
            user.setUserCode(userCode);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            user.setGender(Integer.valueOf(gender));
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            user.setPhone(phone);
            user.setAddress(address);
            user.setUserRole(Integer.valueOf(userRole));
            user.setCreationDate(new Date());
            user.setCreatedBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());

            UserService userService = new UserServiceImpl();
            if(userService.addUser(user)){
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
            }else{
                req.getRequestDispatcher("/jsp/useradd.jsp").forward(req, resp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void ucexist(HttpServletRequest req, HttpServletResponse resp){
        String userCode = req.getParameter("userCode");


        Map<String, String> resultMap = new HashMap<>();
        UserServiceImpl userService = new UserServiceImpl();

        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "exist");
        }else{
            if(userService.userIsExist(userCode)){
                resultMap.put("userCode", "exist");
            }else{
                resultMap.put("userCode", "noExist");
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

    private void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        int id = 0;
        if(!StringUtils.isNullOrEmpty(uid)){
            id = Integer.parseInt(uid);
        }

        Map<String, String> resultMap = new HashMap<>();
        if(id <= 0 || ((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resultMap.put("delResult", "notexist");
        }else{
            UserServiceImpl userService = new UserServiceImpl();
            if(userService.delUserById(id)){
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

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String uid = req.getParameter("uid");

        if(!StringUtils.isNullOrEmpty(uid)){
            int id = Integer.parseInt(uid);
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void modifyUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        if(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getUserRole() != 1){
            resp.sendRedirect(req.getContextPath()+"/jsp/nolimits.jsp");
            return;
        }

        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");


        User user = new User();
        try {
            user.setId(Integer.valueOf(id));
            user.setUserName(userName);
            user.setGender(Integer.parseInt(gender));
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            user.setPhone(phone);
            user.setAddress(address);
            user.setUserRole(Integer.parseInt(userRole));
            user.setModifyBy(((User)req.getSession().getAttribute(Constant.USER_SESSION)).getId());
            user.setModifyDate(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserService userService = new UserServiceImpl();
        if (userService.modifyUser(user)) {
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req, resp);
        }

    }

    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<Role> roleList = new LinkedList<>();

        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(roleList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
