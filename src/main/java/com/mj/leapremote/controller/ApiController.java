package com.mj.leapremote.controller;

import com.alibaba.fastjson.JSONObject;
import com.mj.leapremote.model.User;
import com.mj.leapremote.util.HttpUtils;
import com.mj.leapremote.util.SendEmail;
import com.mj.leapremote.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class ApiController {

    @ResponseBody
    @RequestMapping(value = "/checkvd")
    public String checkvd(@RequestParam("callback") String callback) {
        JSONObject result = new JSONObject();
        result.put("errno", 0);
        result.put("v", "电脑版: "+CoreController.coreController.infoService.getByDescription("最新版本").getContent()
                +" | 安卓版: "+CoreController.coreController.infoService.getByDescription("最新版本安卓").getContent());
        result.put("d", "暂无数据");
        return callback+"("+result+")";
    }

    @ResponseBody
    @RequestMapping(value = "/changelog")
    public String changelog(@RequestParam("callback") String callback) {
        JSONObject result = new JSONObject();
        result.put("errno", 0);
        result.put("b", "电脑版: "+CoreController.coreController.infoService.getByDescription("最新版本描述").getContent()
                +" | 安卓版: "+CoreController.coreController.infoService.getByDescription("最新版本描述安卓").getContent());
        result.put("l", "暂无数据");
        return callback+"("+result+")";
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(@RequestParam("callback") String callback, HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        String password = Utils.urlString(request.getParameter("password"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user = null;
        if(password.isEmpty() || username.isEmpty())
            errno = 1;
        else {
            user = UserController.userController.userService.getUserByUsernameAndPassword(username, password);
            if(user ==null)
                user = UserController.userController.userService.getUserByEmailAndPassword(username, password);
            if(user ==null)
                errno = 2;
        }
        result.put("errno", errno);
        result.put("user", user ==null?"":JSONObject.toJSONString(user));
        if(user !=null) {
            HttpUtils.setCookies(request, response, user);
        }
        return callback+"("+result+")";
    }

    @ResponseBody
    @RequestMapping(value = "/register")
    public String register(@RequestParam("callback") String callback, HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        final String email = Utils.urlString(request.getParameter("email"));
        String password = Utils.urlString(request.getParameter("password"));
        String code = Utils.urlString(request.getParameter("code"));
        String getCode = Utils.urlString(request.getParameter("getCode"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user = null;
        if(getCode.equals("1")) {
            if(!Utils.isEmail(email))
                errno = 1;
            else {
                if(UserController.userController.userService.getUserByEmail(email)!=null)
                    errno = 3;
                else {
                    errno = 4;
                    if (UserController.userController.codeMap.get(email) != null)
                        UserController.userController.codeMap.remove(email);
                    String c = Utils.getRandomNum(4);
                    UserController.userController.codeMap.put(email, c);
                    new Thread(() -> {
                        try {
                            Thread.sleep(900000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        UserController.userController.codeMap.remove(email);
                    }).start();
                    SendEmail.sendEmail(email, c);
                }
            }
        }else if(password.isEmpty() || username.isEmpty() || email.isEmpty() || !Utils.check(username, password) || !Utils.isEmail(email))
            errno = 1;
        else if(UserController.userController.userService.getUserByUsername(username)!=null) {
            errno = 2;
        }else if(UserController.userController.userService.getUserByEmail(email)!=null)
            errno = 3;
        else if(!code.equals(UserController.userController.codeMap.get(email)))
            errno = 5;
        else {
            user = new User();
            user.setUserId(Utils.getRandomUserId(UserController.userController.userService));
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setCreateTime(System.currentTimeMillis());
            user.setMoney(100);
            user.setVip(System.currentTimeMillis()+ Long.parseLong(CoreController.coreController.infoService.getByDescription("试用Vip时长").getContent()));
            UserController.userController.userService.insert(user);
        }
        result.put("errno", errno);
        result.put("user", user ==null?"":JSONObject.toJSONString(user));
        if(user !=null)
            HttpUtils.setCookies(request, response, user);
        return callback+"("+result+")";
    }

    @ResponseBody
    @RequestMapping(value = "/exit")
    public String exit(@RequestParam("callback") String callback, HttpServletResponse response) {
        Cookie cookie = new Cookie("username", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("password", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        JSONObject result = new JSONObject();
        result.put("errno", 0);
        return callback+"("+result+")";
    }
}
