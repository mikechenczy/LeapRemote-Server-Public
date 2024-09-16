package com.mj.leapremote.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.global.control.ControlService;
import com.mj.leapremote.model.UserData;
import com.mj.leapremote.model.Control;
import com.mj.leapremote.util.Utils;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Handler {
    public static final ConcurrentHashMap<String, Session> handles = new ConcurrentHashMap<>();
    public static final List<String> enabledDevices = new ArrayList<>();
    public static final List<String> directEnabledDevices = new ArrayList<>();

    public static UserData putHandler(String deviceId, Session session) {
        if(handles.containsKey(deviceId)) {
            try {
                removeHandler(deviceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handles.put(deviceId, session);
        log.info("建立连接完成,当前在线人数为：{}", count());
        UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
        if(userData!=null) {
            return userData;
        }
        userData = new UserData();
        userData.setDeviceId(deviceId);
        userData.setUserId(0);
        userData.setConnectId(UserController.userController.userDataService.generateConnectId());
        userData.setConnectPin(UserController.userController.userDataService.generateConnectPin());
        userData.setControlId(0);
        UserController.userController.userDataService.insert(userData);
        return userData;
    }

    public static Session getHandler(String deviceId) {
        return handles.get(deviceId);
    }

    public static boolean containsConnectId(String connectId) {
        synchronized (handles) {
            for(String deviceId : handles.keySet()) {
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if(userData.getConnectId().equals(connectId))
                    return true;
            }
            return false;
        }
    }

    public static boolean removeHandler(String deviceId) throws IOException {
        if (handles.containsKey(deviceId)) {
            Session session = handles.get(deviceId);
            if (session!=null && session.isOpen()) {
                session.close();
            }
            handles.remove(deviceId);
            enabledDevices.remove(deviceId);
            directEnabledDevices.remove(deviceId);
            UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
            if (userData != null && userData.getControlId() != 0) {
                Control control = ControlService.get(userData.getControlId());
                if (control != null) {
                    control.removeControl(deviceId);
                    userData.setControlId(0);
                    UserController.userController.userDataService.update(userData);
                }
            }
            new Thread(() -> Handler.refreshDevicesAt(deviceId)).start();
            return true;
        }
        return false;
    }

    public static int count() {
        return handles.size();
    }

    public static void refreshAllDevices() {
        synchronized (handles) {
            for (String deviceId : handles.keySet()) {
                new Thread(() -> refreshDevicesFor(deviceId)).start();
            }
        }
    }

    public static void refreshDevicesAt(String id) {
        for (String deviceId : handles.keySet()) {
            new Thread(() -> {
                Session session = handles.get(deviceId);
                if (session == null || !session.isOpen())
                    return;
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if (userData == null || Utils.stringIsEmpty(userData.getDevices()))
                    return;
                JSONArray targetDevices = JSON.parseArray(userData.getDevices());
                boolean needRefresh = false;
                for(int i=0;i<targetDevices.size();i++) {
                    JSONObject device = targetDevices.getJSONObject(i);
                    if (!device.containsKey("deviceId"))
                        continue;
                    if(device.getString("deviceId").equals(id))
                        needRefresh = true;
                }
                if(!needRefresh) {
                    return;
                }
                refreshDevicesFor(deviceId);
            }).start();
        }
    }

    public static void refreshDevicesFor(String deviceId) {
        Session session = handles.get(deviceId);
        if (session == null || !session.isOpen())
            return;
        UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
        if (userData == null || Utils.stringIsEmpty(userData.getDevices()))
            return;
        JSONArray devices = new JSONArray();
        JSONArray targetDevices = JSON.parseArray(userData.getDevices());
        for(int i=0;i<targetDevices.size();i++) {
            JSONObject d = targetDevices.getJSONObject(i);
            if (!d.containsKey("deviceId"))
                continue;
            String id = d.getString("deviceId");
            UserData ud = UserController.userController.userDataService.getUserDataByDeviceId(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", id);
            jsonObject.put("connectId", d.getString("connectId"));
            jsonObject.put("connectPin", d.getString("connectPin"));
            jsonObject.put("status", Utils.getStatus(id, userData.getVersion()));
            if (ud == null) {
                jsonObject.put("name", "不存在该设备");
                devices.add(jsonObject);
                continue;
            }
            if (ud.getDeviceInfo() == null) {
                jsonObject.put("name", "未知名称");
                devices.add(jsonObject);
                continue;
            }
            try {
                JSONObject deviceInfo = JSON.parseObject(ud.getDeviceInfo());
                jsonObject.put("name", deviceInfo.getString("model"));
                devices.add(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("name", "未知名称");
                devices.add(jsonObject);
            }
        }
        JSONObject result = new JSONObject();
        result.put("type", "devices");
        result.put("devices", devices);
        Utils.sendMessage(session, result.toString(), new Utils.OnSendFailedListener() {
            @Override
            public void onSendFailed(Session session, String message, IOException e) {
                e.printStackTrace();
                try {
                    String success = Handler.removeHandler(deviceId) ? "成功" : "失败";
                    log.info("连接断开,Handler移除session：{}，当前在线人数为：{}", success, Handler.count());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
