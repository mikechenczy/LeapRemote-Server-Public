package com.mj.leapremote.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.global.control.ControlService;
import com.mj.leapremote.server.Handler;
import com.mj.leapremote.util.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Control {
    private int controlId;
    private int connectId;
    private List<String> controllerDeviceIds = new ArrayList<>();
    private String controlledDeviceId;
    private boolean controlling;

    public boolean removeControl(String deviceId) throws IOException {
        if(controlledDeviceId.equals(deviceId)) {
            ControlService.removeControl(controlId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "controlRemoved");
            jsonObject.put("controlled", false);
            jsonObject.put("controlId", controlId);
            UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
            if(userData!=null) {
                userData.setControlId(0);
                UserController.userController.userDataService.insertOrUpdate(userData);
            }
            for(String d : controllerDeviceIds) {
                userData = UserController.userController.userDataService.getUserDataByDeviceId(d);
                if(userData!=null) {
                    userData.setControlId(0);
                    UserController.userController.userDataService.insertOrUpdate(userData);
                }
                Session chx = Handler.getHandler(d);
                Utils.sendMessageSync(chx, jsonObject.toString());
            }
            return true;
        }
        for(int i = 0; i< controllerDeviceIds.size(); i++) {
            if(controllerDeviceIds.get(i).equals(deviceId)) {
                controllerDeviceIds.remove(i);
                checkRemove(deviceId);
                return true;
            }
        }
        return false;
    }

    public boolean containsController(String deviceId) {
        for(String i : controllerDeviceIds) {
            if(i.equals(deviceId)) {
                return true;
            }
        }
        return false;
    }

    private void checkRemove(String deviceId) throws IOException {
        if(controllerDeviceIds.size()==0) {
            ControlService.removeControl(controlId);
            UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(controlledDeviceId);
            if(userData!=null) {
                userData.setControlId(0);
                UserController.userController.userDataService.insertOrUpdate(userData);
            }
            userData = UserController.userController.userDataService.getUserDataByDeviceId(deviceId);
            if(userData!=null) {
                userData.setControlId(0);
                UserController.userController.userDataService.insertOrUpdate(userData);
            }
            notifyControlRemoved();
        }
    }

    public void enterControl(String deviceId) {
        controllerDeviceIds.add(deviceId);
        ControlService.updateControl(this);
    }

    public void notifyControlEstablished() {
        Session chx = Handler.getHandler(controlledDeviceId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "control");
        jsonObject.put("controlled", true);
        jsonObject.put("controlId", controlId);
        Utils.sendMessage(chx, jsonObject.toString(), null);
        jsonObject.replace("controlled", false);
        JSONObject info = JSON.parseObject(UserController.userController.userDataService.getUserDataByDeviceId(controlledDeviceId).getDeviceInfo());
        jsonObject.put("width", info.getInteger("width"));
        jsonObject.put("height", info.getInteger("height"));
        for(String deviceId : controllerDeviceIds) {
            chx = Handler.getHandler(deviceId);
            Utils.sendMessage(chx, jsonObject.toString(), null);
        }
    }

    public void notifyControlRemoved() {
        Session chx = Handler.getHandler(controlledDeviceId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "controlRemoved");
        jsonObject.put("controlled", true);
        Utils.sendMessage(chx, jsonObject.toString(), null);
        jsonObject.replace("controlled", false);
        for(String deviceId : controllerDeviceIds) {
            chx = Handler.getHandler(deviceId);
            Utils.sendMessage(chx, jsonObject.toString(), null);
        }
    }

    public void sendData(boolean controlled, JSONObject data) throws IOException {
        if(!controlled) {
            Session chx = Handler.getHandler(controlledDeviceId);
            chx.getBasicRemote().sendBinary(ByteBuffer.wrap(data.toString().getBytes(StandardCharsets.UTF_8)));
            return;
        }
        for(String deviceId : controllerDeviceIds) {
            Session chx = Handler.getHandler(deviceId);
            chx.getBasicRemote().sendBinary(ByteBuffer.wrap(data.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }
}
