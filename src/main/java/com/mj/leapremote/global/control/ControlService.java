package com.mj.leapremote.global.control;

import com.mj.leapremote.model.Control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControlService {
    public static int getRandomControlId() {
        int controlId = new Random().nextInt(90000000)+10000000;
        if(get(controlId)!=null)
            return getRandomControlId();
        return controlId;
    }

    public static void createControl(Control control) {
        add(control);
    }

    public static int containsDeviceId(String deviceId) {
        List<Control> controls = getAll();
        for(Control control : controls) {
            if(control.getControlledDeviceId().equals(deviceId)) {
                return 1;
            }
            if(control.getControllerDeviceIds().contains(deviceId)) {
                return 2;
            }
        }
        return 0;
    }

    public static int getControlIdFromDeviceId(String deviceId) {
        List<Control> controls = getAll();
        for(Control control : controls) {
            if(control.getControlledDeviceId().equals(deviceId)) {
                return control.getControlId();
            }
            if(control.getControllerDeviceIds().contains(deviceId)) {
                return control.getControlId();
            }
        }
        return 0;
    }

    public static boolean containsControlId(int controlId) {
        List<Control> controls = getAll();
        for(Control control : controls) {
            if(control.getControlId()==controlId) {
                return true;
            }
        }
        return false;
    }

    public static Control getControl(int controlId) {
        return get(controlId);
    }

    public static boolean updateControl(Control control) {
        return update(control);
    }

    public static void removeControl(int controlId) {
        remove(controlId);
    }

    private static List<Control> controls = new ArrayList<>();

    public static List<Control> getAll() {
        return controls;
    }

    public static Control get(int controlId) {
        for(Control control : controls) {
            if(control.getControlId()==controlId)
                return control;
        }
        return null;
    }

    public static void add(Control control) {
        controls.add(control);
    }

    public static void delete(int controlId) {
        for(int i=0;i<controls.size();i++) {
            if(controls.get(i).getControlId()==controlId) {
                controls.remove(i);
                return;
            }
        }
    }

    public static boolean update(Control control) {
        for(int i=0;i<controls.size();i++) {
            if(controls.get(i).getControlId()==control.getControlId()) {
                controls.set(i, control);
                return true;
            }
        }
        return false;
    }

    public static boolean remove(int controlId) {
        for(int i=0;i<controls.size();i++) {
            if(controls.get(i).getControlId()==controlId) {
                controls.remove(i);
                return true;
            }
        }
        return false;
    }

    public static Control getControlFromDeviceId(String deviceId) {
        return getControl(getControlIdFromDeviceId(deviceId));
    }
}
