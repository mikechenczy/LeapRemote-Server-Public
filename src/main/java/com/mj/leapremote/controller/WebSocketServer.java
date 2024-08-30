package com.mj.leapremote.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mj.leapremote.global.control.ControlService;
import com.mj.leapremote.model.Control;
import com.mj.leapremote.model.UserData;
import com.mj.leapremote.server.ControlHandler;
import com.mj.leapremote.server.Handler;
import com.mj.leapremote.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author： xxt
 * @date： 2022/5/23 16:27
 * @Description： WebSocket操作类
 */
@ServerEndpoint("/websocket/{deviceId}")
@Component
@Slf4j
public class WebSocketServer {

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String deviceId;


    /**
     * 建立WebSocket连接
     *
     * @param deviceId 用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "deviceId") String deviceId) throws IOException {
        log.info("WebSocket建立连接中,连接设备ID：{}", deviceId);
        this.session = session;
        this.deviceId = deviceId;
        if(deviceId==null) {
            session.close();
            return;
            //TODO: Send Message
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "connectIdAndPin");
        Utils.OnSendFailedListener onSendFailedListener = new Utils.OnSendFailedListener() {
            @Override
            public void onSendFailed(Session session, String message, IOException e) {
                //e.printStackTrace();
                try {
                    Session s = Handler.getHandler(deviceId);
                    //TODO: Send Message
                    s.close();
                    Handler.removeHandler(deviceId);
                    UserData userData = Handler.putHandler(deviceId, session);
                    jsonObject.put("connectId", userData.getConnectId());
                    jsonObject.put("connectPin", userData.getConnectPin());
                    Utils.sendMessage(session, jsonObject.toString(), null);
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        };
        UserData userData = Handler.putHandler(deviceId, session);
        jsonObject.put("connectId", userData.getConnectId());
        jsonObject.put("connectPin", userData.getConnectPin());
        Utils.sendMessage(session, jsonObject.toString(), onSendFailedListener);
        jsonObject.put("type", "speedLimit");
        jsonObject.put("speedLimited", false);
        jsonObject.put("maxSpeed", 1024*1024);
        Utils.sendMessage(session, jsonObject.toString(), onSendFailedListener);
        new Thread(() -> {
            while (session.isOpen()) {
                try {
                    //System.out.println("SEND");
                    Utils.sendMessageSync(session, "{\"type\": \"isOnline\"}");
                } catch (Exception e) {
                    //e.printStackTrace();
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        //throwable.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() throws IOException {
        String success = Handler.removeHandler(deviceId)?"成功":"失败";
        log.info("连接断开,Handler移除session：{}，当前在线人数为：{}", success, Handler.count());
    }

    /**
     * 接收客户端消息
     *
     * @param message 接收的消息
     */
    @OnMessage(maxMessageSize=2155380*10)
    public void onMessage(String message) throws IOException {
        //log.info("收到客户端发来的消息：{}", message);
        if(message.equals("heartbeat"))
            return;
        JSONObject request = JSON.parseObject(message);
        switch (request.getString("type")) {
            case "deviceRemove": {
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if(userData==null)
                    return;
                if(!request.containsKey("deviceId"))
                    return;
                JSONArray jsonArray = JSON.parseArray(userData.getDevices());
                for(int i=0;i<jsonArray.size();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(request.getString("deviceId").equals(jsonObject.getString("deviceId"))) {
                        jsonArray.remove(i);
                        break;
                    }
                }
                userData.setDevices(jsonArray.toString());
                UserController.userController.userDataService.update(userData);
                Handler.refreshDevices(deviceId);
                break;
            }
            case "remote": {
                if(request.getBooleanValue("directEnabled")) {
                    if(!Handler.directEnabledDevices.contains(deviceId)) {
                        Handler.directEnabledDevices.add(deviceId);
                    }
                } else {
                    Handler.directEnabledDevices.remove(deviceId);
                }
                if(request.getBooleanValue("enabled")) {
                    if(!Handler.enabledDevices.contains(deviceId)) {
                        Handler.enabledDevices.add(deviceId);
                        Handler.refreshDevices();
                    }
                } else {
                    if(!Handler.enabledDevices.contains(deviceId)) {
                        return;
                    }
                    UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                    if (userData != null && userData.getControlId() != 0) {
                        Control control = ControlService.get(userData.getControlId());
                        if (control != null) {
                            control.removeControl(deviceId);
                            userData.setControlId(0);
                            UserController.userController.userDataService.update(userData);
                        }
                    }
                    Handler.enabledDevices.remove(deviceId);
                    Handler.refreshDevices();
                }
                break;
            }
            case "basicData": {
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if(userData==null)
                    return;
                userData.setLastLogin(System.currentTimeMillis());
                userData.setVersion(request.getString("version"));
                userData.setIp(request.getString("ip"));
                userData.setDeviceInfo(request.getString("deviceInfo"));
                UserController.userController.userDataService.update(userData);
                Handler.refreshDevices();
                break;
            }
            case "devices": {
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if(userData==null)
                    return;
                if(request.containsKey("devices")) {
                    userData.setDevices(request.getString("devices"));
                    UserController.userController.userDataService.update(userData);
                }
                Handler.refreshDevices(deviceId);
                break;
            }
            case "send": {
                int controlId = request.getInteger("controlId");
                Control control = ControlService.getControl(controlId);
                if (control == null) {
                    JSONObject response = new JSONObject();
                    response.put("type", "sendFailed");
                    response.put("controlled", request.getBoolean("controlled"));
                    response.put("reason", "控制失效");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                control.sendData(request.getBoolean("controlled"), request.getJSONObject("data"));
                break;
            }
            case "changePin" : {
                UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
                if(userData==null)
                    return;
                userData.setConnectPin(UserController.userController.userDataService.generateConnectPin());
                UserController.userController.userDataService.update(userData);
                JSONObject response = new JSONObject();
                response.put("type", "connectIdAndPin");
                response.put("connectId", userData.getConnectId());
                response.put("connectPin", userData.getConnectPin());
                Utils.sendMessageSync(session, response.toString());
                break;
            }
            case "connect": {
                if (ControlService.containsDeviceId(deviceId) != 0) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "您已在控制");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }

                UserData userData = UserController.userController.userDataService.getUserDataByConnectId(request.getString("connectId"));
                if (userData == null) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "控制码不存在");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                if(!userData.getConnectPin().equals(request.getString("connectPin"))) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "控制密码错误");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                if(!Handler.handles.containsKey(userData.getDeviceId())) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "此设备离线");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                if(userData.getDeviceId().equals(deviceId)) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "不能控制自己");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                if (ControlService.containsDeviceId(userData.getDeviceId()) == 1) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "对方正在被控制");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                if(Handler.directEnabledDevices.contains(userData.getDeviceId()) && !request.getBooleanValue("forward")) {
                    if(userData.getIps()!=null) {
                        JSONArray hosts = JSON.parseArray(userData.getIps());
                        if(hosts.size()>0) {
                            JSONObject response = new JSONObject();
                            response.put("type", "controlForward");
                            response.put("reason", "此设备开启了直连模式且拥有公网ip，为提升您的使用建议您直连模式连接");
                            response.put("availableHosts", hosts);
                            Utils.sendMessageSync(session, response.toString());
                            return;
                        }
                    }
                }
                if(!Handler.enabledDevices.contains(userData.getDeviceId())) {
                    JSONObject response = new JSONObject();
                    response.put("type", "controlFailed");
                    response.put("reason", "此设备未开启连接码模式的远程控制");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }

                if (userData.getControlId() != 0) {
                    ControlHandler.enterControl(userData.getControlId(), deviceId);
                    JSONObject response = new JSONObject();
                    response.put("type", "control");
                    response.put("controlled", false);
                    response.put("controlId", userData.getControlId());
                    JSONObject info = JSON.parseObject(userData.getDeviceInfo());
                    response.put("width", info.getInteger("width"));
                    response.put("height", info.getInteger("height"));
                    Utils.sendMessageSync(session, response.toString());
                } else {
                    ControlHandler.establishNewControl(userData.getDeviceId(), deviceId);
                }
                break;
            }
            case "exitControl": {
                if (ControlService.containsDeviceId(deviceId) == 0) {
                    JSONObject response = new JSONObject();
                    response.put("type", "exitFailed");
                    response.put("reason", "您不在控制");
                    Utils.sendMessageSync(session, response.toString());
                    return;
                }
                System.out.println("EXIT");
                ControlService.getControlFromDeviceId(deviceId).removeControl(deviceId);
                break;
            }
        }
    }
}

