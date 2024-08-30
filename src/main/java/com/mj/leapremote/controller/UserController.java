package com.mj.leapremote.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.mj.leapremote.model.LoginInfo;
import com.mj.leapremote.model.User;
import com.mj.leapremote.service.UserDataService;
import com.mj.leapremote.service.UserService;
import com.mj.leapremote.threads.DeleteUserThread;
import com.mj.leapremote.util.HttpUtils;
import com.mj.leapremote.util.IpAdrressUtil;
import com.mj.leapremote.util.SendEmail;
import com.mj.leapremote.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    public HashMap<String, String> codeMap = new HashMap<>();

    @Autowired
    public UserService userService;

    @Autowired
    public UserDataService userDataService;

    public static UserController userController;

    @PostConstruct
    public void init() {
        userController = this;
        userController.userService = userService;
        userController.userDataService = userDataService;
    }

    @ResponseBody
    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        String password = Utils.urlString(request.getParameter("password"));
        String ipAddress = request.getParameter("ipAddress");
        String device = request.getParameter("device");
        String version = request.getParameter("version");
        JSONObject result = new JSONObject();
        int errno = 0;
        User user = null;
        if(password.isEmpty() || username.isEmpty())
            errno = 1;
        else {
            user = userService.getUserByUsernameAndPassword(username, password);
            if(user ==null)
                user = userService.getUserByEmailAndPassword(username, password);
            if(user ==null)
                errno = 2;
        }
        if(errno==0) {
            User finalUser = user;
            new Thread(() -> {
                String province;
                String countryAndProvince;
                if(!StringUtils.isEmpty(ipAddress)) {
                    try {
                        province = IpAdrressUtil.getProvince(ipAddress);
                        countryAndProvince = IpAdrressUtil.getCountry(ipAddress)+province;
                    } catch (IOException | GeoIp2Exception e) {
                        e.printStackTrace();
                        province = e.toString();
                        countryAndProvince = e.toString();
                    }
                } else {
                    province = "null";
                    countryAndProvince = "null";
                }
                LoginInfo loginInfo = CoreController.coreController.loginInfoService.getByUserId(finalUser.getUserId());
                if(loginInfo==null) {
                    loginInfo = new LoginInfo();
                    loginInfo.setUserId(finalUser.getUserId());
                    loginInfo.setUsername(finalUser.getUsername());
                    loginInfo.setLastTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    if(!StringUtils.isEmpty(version)) {
                        loginInfo.setVersion(version);
                    }
                    if(!StringUtils.isEmpty(device)) {
                        loginInfo.setDevice(device);
                    }
                    loginInfo.setLocation(province);
                    CoreController.coreController.loginInfoService.insert(loginInfo);
                } else {
                    if(!StringUtils.isEmpty(device)) {
                        loginInfo.setDevice(device);
                    }
                    if(!StringUtils.isEmpty(version)) {
                        loginInfo.setVersion(version);
                    }
                    loginInfo.setLocation(province);
                    loginInfo.setLastTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    CoreController.coreController.loginInfoService.update(loginInfo);
                }
                finalUser.setFinalLogin(System.currentTimeMillis());
                if(!StringUtils.isEmpty(device)) {
                    if(!StringUtils.isEmpty(finalUser.getDevice())) {
                        String[] devices = finalUser.getDevice().split(", ");
                        int updateIndex = -1;
                        List<String> newer = new ArrayList<>();
                        for(int i=0;i<devices.length;i++) {
                            if(devices[i].startsWith(device)) {
                                updateIndex = i;
                                break;
                            }
                        }
                        newer.add(device+(!StringUtils.isEmpty(version)?version:""));
                        if(updateIndex==-1) {
                            Collections.addAll(newer, devices);
                        } else {
                            for(int i=0;i<devices.length;i++) {
                                if(i==updateIndex)
                                    continue;
                                newer.add(devices[i]);
                            }
                        }
                        StringBuilder update = new StringBuilder();
                        for(int i=0;i<newer.size();i++) {
                            update.append(newer.get(i));
                            if(i!=newer.size()-1) {
                                update.append(", ");
                            }
                        }
                        finalUser.setDevice(update.toString());
                    } else {
                        finalUser.setDevice(device+(!StringUtils.isEmpty(version)?version:""));
                    }
                }
                finalUser.setIp(countryAndProvince+(!StringUtils.isEmpty(ipAddress)?ipAddress:""));
                userService.update(finalUser);
                DeleteUserThread.update(finalUser);
            }).start();
        }
        result.put("errno", errno);
        result.put("user", user==null?"":JSONObject.toJSONString(user));
        if(user != null) {
            JSONArray examiners = JSON.parseArray(CoreController.coreController.infoService.getByDescription("审核员名单").getContent());
            if(examiners.contains(user.getUsername())) {
                result.put("isExamining", true);
            }
            HttpUtils.setCookies(request, response, user);
        }
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/loginWithoutData", produces = "application/json;charset=UTF-8")
    public String loginWithoutData(HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        String password = Utils.urlString(request.getParameter("password"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user = null;
        if(password.isEmpty() || username.isEmpty())
            errno = 1;
        else {
            user = userService.getUserByUsernameAndPassword(username, password);
            if(user ==null)
                user = userService.getUserByEmailAndPassword(username, password);
            if(user ==null)
                errno = 2;
        }
        result.put("errno", errno);
        result.put("user", user ==null?"":JSONObject.toJSONString(user));
        if(user !=null) {
            HttpUtils.setCookies(request, response, user);
        }
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        final String email = Utils.urlString(request.getParameter("email"));
        String password = Utils.urlString(request.getParameter("password"));
        String code = Utils.urlString(request.getParameter("code"));
        String getCode = Utils.urlString(request.getParameter("getCode"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user = null;
        if(getCode.equals("1"))
            if (!Utils.isEmail(email))
                errno = 1;
            else
            if (userService.getUserByEmail(email)!=null)
                errno = 3;
            else {
                errno = 4;
                if (codeMap.get(email) != null)
                    codeMap.remove(email);
                String c = Utils.getRandomNum(4);
                codeMap.put(email, c);
                new Thread(() -> {
                    try {
                        Thread.sleep(900000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    codeMap.remove(email);
                }).start();
                new Thread(() -> SendEmail.sendEmail(email, c)).start();
            }
        else if(password.isEmpty() || username.isEmpty() || email.isEmpty() || !Utils.check(username, password) || !Utils.isEmail(email))
            errno = 1;
        else if(userService.getUserByUsername(username)!=null)
            errno = 2;
        else if(userService.getUserByEmail(email)!=null)
            errno = 3;
        else if(!code.equals(codeMap.get(email)))
            errno = 5;
        else {
            user = new User();
            user.setUserId(Utils.getRandomUserId(userService));
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setCreateTime(System.currentTimeMillis());
            user.setVip(System.currentTimeMillis()+ Long.parseLong(CoreController.coreController.infoService.getByDescription("试用Vip时长").getContent()));
            user.setVipType(1);
            user.setFinalLogin(System.currentTimeMillis());
            userService.insert(user);
        }
        result.put("errno", errno);
        result.put("user", user ==null?"":JSONObject.toJSONString(user));
        if(user !=null) {
            HttpUtils.setCookies(request, response, user);
            DeleteUserThread.update(user);
        }
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        if (Utils.isUser(request))
            request.getSession().removeAttribute("user");
        return "true";
    }

    @ResponseBody
    @RequestMapping(value = "/forgetPasswordEmail")
    public String forgetPasswordEmail(HttpServletRequest request) {
        String username = Utils.urlString(request.getParameter("username"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user;
        if(username.isEmpty())
            errno = 1;
        else {
            user = userService.getUserByUsername(username);
            if (user == null)
                user = userService.getUserByEmail(username);
            if (user == null)
                errno = 2;
            else {
                String email = user.getEmail();
                if (codeMap.get(email) != null)
                    codeMap.remove(email);
                String c = Utils.getRandomNum(4);
                codeMap.put(email, c);
                new Thread(() -> {
                    try {
                        Thread.sleep(900000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    codeMap.remove(email);
                }).start();
                new Thread(() -> SendEmail.sendEmail(email, c)).start();
            }
        }
        result.put("errno", errno);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/forgetPassword", produces = "application/json;charset=UTF-8")
    public String forgetPassword(HttpServletRequest request, HttpServletResponse response) {
        String username = Utils.urlString(request.getParameter("username"));
        String password = Utils.urlString(request.getParameter("password"));
        String code = Utils.urlString(request.getParameter("code"));
        JSONObject result = new JSONObject();
        int errno = 0;
        User user;
        if(password.isEmpty() || username.isEmpty() || !Utils.check(password))
            errno = 1;
        else {
            user = userService.getUserByUsername(username);
            if(user==null)
                user = userService.getUserByEmail(username);
            if(user==null)
                errno = 2;
            else if(code.equals(codeMap.get(user.getEmail()))) {
                user.setPassword(password);
                userService.update(user);
                HttpUtils.setCookies(request, response, user);
            } else {
                errno = 3;
            }
        }
        result.put("errno", errno);
        return result.toString();
    }
}