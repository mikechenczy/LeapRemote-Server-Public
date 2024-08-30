package com.mj.leapremote.server;

import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.global.control.ControlService;
import com.mj.leapremote.model.Control;
import com.mj.leapremote.model.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Mike_Chen
 * @date 2023/6/24
 * @apiNote
 */
public class ControlHandler {
    public static synchronized void establishNewControl(String controlledDeviceId, String controllerDeviceId) throws IOException {
        Control control = new Control();
        control.setControlId(ControlService.getRandomControlId());
        control.setControlledDeviceId(controlledDeviceId);
        control.setControllerDeviceIds(new ArrayList<>(Collections.singletonList(controllerDeviceId)));
        ControlService.createControl(control);
        UserData userData = UserController.userController.userDataService.getUserDataByDeviceId(controlledDeviceId);
        userData.setControlId(control.getControlId());
        UserController.userController.userDataService.insertOrUpdate(userData);
        userData = UserController.userController.userDataService.getUserDataByDeviceId(controllerDeviceId);
        userData.setControlId(control.getControlId());
        UserController.userController.userDataService.insertOrUpdate(userData);
        control.notifyControlEstablished();
    }

    public static void enterControl(int controlId, String controllerDeviceId) {
        if(controlId==0)
            return;
        Control control = ControlService.get(controlId);
        if(control==null)
            return;
        control.enterControl(controllerDeviceId);
    }
}
