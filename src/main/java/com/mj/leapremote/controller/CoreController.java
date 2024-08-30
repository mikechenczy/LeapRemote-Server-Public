package com.mj.leapremote.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mj.leapremote.model.*;
import com.mj.leapremote.server.Handler;
import com.mj.leapremote.service.InfoService;
import com.mj.leapremote.service.LoginInfoService;
import com.mj.leapremote.service.SignInService;
import com.mj.leapremote.util.PingUtil;
import com.mj.leapremote.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/core")
public class CoreController {
    @Autowired
    public InfoService infoService;

    @Autowired
    public SignInService signInService;

    @Autowired
    public LoginInfoService loginInfoService;

    public static CoreController coreController;

    @PostConstruct
    public void init() {
        coreController = this;
        coreController.infoService = infoService;
        coreController.signInService = signInService;
        coreController.loginInfoService = loginInfoService;
    }

    @ResponseBody
    @RequestMapping(value = "/getSignInInfo", produces = "application/json;charset=UTF-8")
    public String getSignInInfo() {
        JSONObject result = new JSONObject();
        result.put("signInInfo", infoService.getByDescription("人类签到时长").getContent());
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/checkVersionNew")
    public String checkVersionNew(HttpServletRequest request) {
        String device = Utils.urlString(request.getParameter("device"));
        String version = Utils.urlString(request.getParameter("version"));
        if(device.startsWith("Java")) {
            String currentVersion = infoService.getByDescription("最新版本Java").getContent();
            String currentVersionDescription = infoService.getByDescription("最新版本描述Java").getContent();
            boolean force = infoService.getByDescription("强制更新Java").getContent().equals("true");
            Info info = infoService.getByDescription(version+"Java");
            if(info!=null) {
                force = info.getContent().equals("true");
            }
            info = infoService.getByDescription(version+"描述Java");
            if(info!=null) {
                currentVersionDescription = info.getContent();
            }
            boolean needUpdate = !version.equals(currentVersion);
            if(version.equals(infoService.getByDescription("预发布版本Java").getContent())) {
                needUpdate = false;
            }
            JSONObject result = new JSONObject();
            result.put("version", currentVersion);
            result.put("description", currentVersionDescription);
            result.put("isExamining", infoService.getByDescription("是否处于审核期").getContent().equals("true"));
            if(needUpdate)
                result.put("force", force);
            return result.toString();
        }
        String currentVersion = infoService.getByDescription("最新版本").getContent();
        String currentVersionDescription = infoService.getByDescription("最新版本描述").getContent();
        boolean force = infoService.getByDescription("强制更新").getContent().equals("true");
        Info info = infoService.getByDescription(version);
        if(info!=null) {
            force = info.getContent().equals("true");
        }
        info = infoService.getByDescription(version+"描述");
        if(info!=null) {
            currentVersionDescription = info.getContent();
        }
        boolean needUpdate = !version.equals(currentVersion);
        if(version.equals(infoService.getByDescription("预发布版本").getContent())) {
            needUpdate = false;
        }
        JSONObject result = new JSONObject();
        result.put("version", currentVersion);
        result.put("description", currentVersionDescription);
        result.put("isExamining", infoService.getByDescription("是否处于审核期").getContent().equals("true"));
        if(needUpdate)
            result.put("force", force);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/checkVersion")
    public String checkVersion(HttpServletRequest request) {
        String version = Utils.urlString(request.getParameter("version"));
        if(version.equals(infoService.getByDescription("预发布版本").getContent())) {
            return "";
        }
        return version.equals(infoService.getByDescription("最新版本").getContent())?"":infoService.getByDescription("最新版本").getContent()+"v/d"+infoService.getByDescription("最新版本描述").getContent();
    }

    @ResponseBody
    @RequestMapping(value = "/checkVersionAndroidNew")
    public String checkVersionAndroidNew(HttpServletRequest request) {
        String version = Utils.urlString(request.getParameter("version"));
        String currentVersion = infoService.getByDescription("最新版本安卓").getContent();
        String currentVersionDescription = infoService.getByDescription("最新版本描述安卓").getContent();
        boolean force = infoService.getByDescription("强制更新安卓").getContent().equals("true");
        Info info = infoService.getByDescription(version+"安卓");
        if(info!=null) {
            force = info.getContent().equals("true");
        }
        info = infoService.getByDescription(version+"安卓描述");
        if(info!=null) {
            currentVersionDescription = info.getContent();
        }
        boolean needUpdate = !version.equals(currentVersion);
        if(version.equals(infoService.getByDescription("预发布版本安卓").getContent())) {
            needUpdate = false;
        }
        JSONObject result = new JSONObject();
        result.put("version", currentVersion);
        result.put("description", currentVersionDescription);
        result.put("downloadUrl", infoService.getByDescription("安卓版下载地址").getContent());
        result.put("isExamining", infoService.getByDescription("是否处于审核期").getContent().equals("true") && version.equals(infoService.getByDescription("预发布版本安卓").getContent()));
        if(needUpdate)
            result.put("force", force);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/checkVersionAndroid")
    public String checkVersionAndroid(HttpServletRequest request) {
        String version = Utils.urlString(request.getParameter("version"));
        if(version.equals(infoService.getByDescription("预发布版本安卓").getContent())) {
            return "";
        }
        return version.equals(infoService.getByDescription("最新版本安卓").getContent())?"":infoService.getByDescription("最新版本安卓").getContent()+"v/d"+infoService.getByDescription("最新版本描述安卓").getContent();
    }

    @ResponseBody
    @RequestMapping(value = "/signIn")
    public String signIn(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        User user = Utils.getUser(request);
        if (user ==null) {
            result.put("errno", 1);
            return result.toString();
        }
        boolean signInned;
        synchronized (signInService) {
            signInned = signInService.getByUserId(String.valueOf(user.getUserId())) != null;
            if(!signInned) {
                signInService.insert(new SignIn(user.getUserId(), user.getUsername()));
            }
        }
        if(signInned) {
            result.put("errno", 2);
            return result.toString();
        }
        if(user.getVip()<=System.currentTimeMillis()) {
            if (user.getVipType() == 1)
                user.setVipType(0);
            user.setVip(System.currentTimeMillis()+Long.parseLong(infoService.getByDescription("签到时长").getContent()));
        } else
            user.setVip(user.getVip()+Long.parseLong(infoService.getByDescription("签到时长").getContent()));
        UserController.userController.userService.update(user);
        result.put("errno", 0);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getPcUrl")
    public String getPcUrl() {
        return CoreController.coreController.infoService.getByDescription("电脑版下载地址").getContent();
    }

    @ResponseBody
    @RequestMapping(value = "/getAndroidUrl")
    public String getAndroidUrl() {
        return CoreController.coreController.infoService.getByDescription("安卓版下载地址").getContent();
    }

    @ResponseBody
    @RequestMapping(value = "/getMessageContent")
    public String getMessageContent(HttpServletRequest request) {
        String version = Utils.urlString(request.getParameter("version"));
        boolean android = Utils.urlString(request.getParameter("device")).equals("android");
        if(version.equals("")) {
            return CoreController.coreController.infoService.getByDescription("公告"+(android?"安卓":"")).getContent();
        } else {
            Info info = CoreController.coreController.infoService.getByDescription("公告"+(android?"安卓":"")+version);
            if(info!=null) {
                return info.getContent();
            } else {
                return CoreController.coreController.infoService.getByDescription("公告"+(android?"安卓":"")).getContent();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getDeviceByConnectId")
    public String getDeviceByConnectId(HttpServletRequest request) {
        String connectId = Utils.urlString(request.getParameter("connectId"));
        if(StringUtils.isEmpty(connectId)) {
            return null;
        }
        JSONObject result = new JSONObject();
        UserData userData = UserController.userController.userDataService.getUserDataByConnectId(connectId);
        if(userData==null) {
            result.put("name", "不存在的设备");
            result.put("status", Device.STATUS_OFFLINE);
            result.put("os", Device.OS_UNKNOWN);
            return result.toString();
        }
        if(userData.getDeviceInfo()!=null) {
            JSONObject jsonObject = JSON.parseObject(userData.getDeviceInfo());
            result.put("name", jsonObject.getString("model"));
            result.put("os", jsonObject.getInteger("operateSystem"));
        }
        result.put("deviceId", userData.getDeviceId());
        result.put("status", Handler.containsConnectId(connectId)?Device.STATUS_ONLINE:Device.STATUS_OFFLINE);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/helpPingIp")
    public String helpPingIp(HttpServletRequest request) {
        String deviceId = Utils.urlString(request.getParameter("deviceId"));
        if(StringUtils.isEmpty(deviceId)) {
            return null;
        }
        UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
        if(userData==null) {
            return null;
        }
        if(request.getParameter("hosts")==null) {
            return null;
        }
        JSONObject result = new JSONObject();
        JSONArray original = JSON.parseArray(request.getParameter("hosts"));
        //System.out.println(original);
        JSONArray then = new JSONArray();
        final int[] finish = new int[1];
        for(int i=0;i<original.size();i++) {
            JSONObject jsonObject = original.getJSONObject(i);
            new Thread(() -> {
                if(PingUtil.isReachable(jsonObject.getString("ip")))
                    then.add(jsonObject);
                finish[0] = finish[0] +1;
            }).start();
        }
        while (finish[0]!=original.size()) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Thread(() -> {
            userData.setIps(then.toString());
            UserController.userController.userDataService.update(userData);
        }).start();
        result.put("result", then);
        //System.out.println(then);
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/publicIp")
    public String publicIp(HttpServletRequest request) {
        String deviceId = Utils.urlString(request.getParameter("deviceId"));
        if(StringUtils.isEmpty(deviceId)) {
            return null;
        }
        UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
        if(userData==null) {
            return null;
        }
        if(request.getParameter("hosts")==null) {
            return null;
        }
        JSONObject result = new JSONObject();
        JSONArray hosts = JSON.parseArray(request.getParameter("hosts"));
        //System.out.println(hosts);
        userData.setIps(hosts.toString());
        UserController.userController.userDataService.update(userData);
        return result.toString();
    }
}