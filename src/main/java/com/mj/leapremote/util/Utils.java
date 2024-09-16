package com.mj.leapremote.util;

import com.mj.leapremote.controller.CoreController;
import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.model.Device;
import com.mj.leapremote.model.User;
import com.mj.leapremote.server.Handler;
import com.mj.leapremote.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class Utils {
    public static boolean stringIsNull(String s) {
        return string(s).isEmpty();
    }

    public static boolean stringIsEmpty(String s) {
        return string(s).replaceAll(" ", "").replaceAll("\r", "").replaceAll("\n", "").isEmpty();
    }

    public static boolean stringsIsNull(String[] strings) {
        if(strings==null)
            return true;
        return stringsIsNull(Arrays.asList(strings));
    }

    public static boolean stringsIsNull(List<String> strings) {
        if(strings==null || strings.size()==0)
            return true;
        for (String s : strings)
            if(stringIsNull(s))
                return true;
        return false;
    }

    public static String string(String s) {
        return s==null?"":s;
    }

    public static String urlString(String s) {
        try {
            return URLDecoder.decode(string(s), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }

    public static int getRandomUserId(UserService userService) {
        int userId = new Random().nextInt(90000000)+10000000;
        if(userService.getUserByUserId(userId)!=null)
            return getRandomUserId(userService);
        return userId;
    }

    public static String getRandomNum(int digit) {
        String result = "";
        for(int i=0;i<digit;i++) {
            result = result + new Random().nextInt(10);
        }
        return result;
    }

    public static boolean check(String username, String password) {
        /*return !(username.contains(" ") || password.contains(" ") || username.equals("") || password.equals("") || username.contains(",") || username.contains("&")
                || username.contains("?") || password.contains("&") || password.contains("?") || username.contains("=") || password.contains("=") || username.contains("/")
                || password.contains("/") || username.contains("\\") || password.contains("\\"));*/
        return !(username.contains(" ") || password.contains(" ") || username.equals("") || password.equals("") || username.length()>=15 || password.length()>=20);
    }

    public static boolean check(String password) {
        return !(password.contains(" ") || password.equals("") || password.length()>=20);
    }

    public static boolean isEmail(String str) {
        boolean isEmail = false;
        String expr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";

        if (str.matches(expr)) {
            isEmail = true;
        }
        if(isEmail) {
            List<String> availableEmails = Arrays.asList(CoreController.coreController.infoService.getByDescription("支持的邮箱").getContent().split(";"));
            for (String availableEmail : availableEmails) {
                if(str.endsWith(availableEmail)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static String encodeString(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }

    public static Integer fileToFileId(File file) {
        if(file==null)
            return null;
        try {
            return Integer.parseInt(file.getName());
        }catch (Exception e) {
            return null;
        }
    }

    public static List<Integer> filesToFileIds(List<File> files) {
        if(files==null)
            return null;
        List<Integer> result = new ArrayList<>();
        for(File file : files) {
            result.add(fileToFileId(file));
        }
        return result;
    }

    public static boolean checkFileName(String fileName) {
        return fileName != null && (!(fileName.contains("\\") || fileName.contains("/") || fileName.contains(":") || fileName.contains("*") || fileName.contains("?")
                || fileName.contains("\"") || fileName.contains("<") || fileName.contains(">") || fileName.contains("|") || fileName.equals("")));
    }

    public static boolean isUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null)
            return false;
        else {
            Object o = session.getAttribute("user");
            return o instanceof User && (UserController.userController.userService.getUserByUserId(((User) o).getUserId()) != null);
        }
    }
    public static User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null)
            return null;
        else {
            Object o = session.getAttribute("user");
            return (o instanceof User && (UserController.userController.userService.getUserByUserId(((User) o).getUserId()) != null))? (User) o :null;
        }
    }

    public static long getTomorrowZeroMillis() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTimeInMillis()- current);
    }

    public static int getStatus(String id, String version) {
        if(!Handler.handles.containsKey(id))
            return Device.STATUS_OFFLINE;
        if(Handler.enabledDevices.contains(id))
            return Device.STATUS_ONLINE;
        if(Handler.directEnabledDevices.contains(id))
            return "1.0".equals(version)?Device.STATUS_ONLINE:Device.STATUS_DIRECT;
        return Device.STATUS_NOT_ENABLED;
    }

    public static abstract class OnSendFailedListener {
        public abstract void onSendFailed(Session session, String message, IOException e);
    }

    public static void sendMessageSync(Session session, String message) throws IOException {
        synchronized (session) {
            if(session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    public static void sendMessage(Session session, String message, OnSendFailedListener onSendFailedListener) {
        new Thread(() -> {
            synchronized (session) {
                if(session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        if (onSendFailedListener != null)
                            onSendFailedListener.onSendFailed(session, message, e);
                    }
                }
            }
        }).start();
    }
}
