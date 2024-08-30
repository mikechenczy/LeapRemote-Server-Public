package com.mj.leapremote.threads;

import com.mj.leapremote.controller.CoreController;
import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.model.User;
import com.mj.leapremote.util.SendEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mike_Chen
 * @date 2023/3/23
 * @apiNote
 */
public class DeleteUserThread extends Thread {
    public static Map<Integer, DeleteUserThread> threads = new HashMap<>();

    public static void startThread() {
        for(User user : UserController.userController.userService.getAll()) {
            if(user.getFinalLogin()==0) {
                user.setFinalLogin(System.currentTimeMillis());
                UserController.userController.userService.update(user);
            }
            DeleteUserThread deleteUserThread = new DeleteUserThread(user);
            threads.put(user.getUserId(), deleteUserThread);
            deleteUserThread.start();
        }
    }

    public static void update(User user) {
        if(threads.containsKey(user.getUserId())) {
            threads.get(user.getUserId()).interrupt();
            threads.remove(user.getUserId());
        }
        DeleteUserThread deleteUserThread = new DeleteUserThread(user);
        threads.put(user.getUserId(), deleteUserThread);
        deleteUserThread.start();
    }

    public User user;

    public DeleteUserThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        long current = System.currentTimeMillis();
        long basicTime = user.getVip()>current?user.getVip():user.getFinalLogin();
        if(current-basicTime<Long.parseLong(CoreController.coreController.infoService.getByDescription("账号删除时长").getContent())) {
            try {
                Thread.sleep(basicTime+Long.parseLong(CoreController.coreController.infoService.getByDescription("账号删除时长").getContent())-current);
            } catch (InterruptedException e) {
                return;
            }
        }
        sendEmail();
        try {
            Thread.sleep(Long.parseLong(CoreController.coreController.infoService.getByDescription("账号删除缓冲时长").getContent()));
        } catch (InterruptedException e) {
            return;
        }
        deleteUser();
        threads.remove(user.getUserId());
    }

    public void sendEmail() {
        //if(user.getEmail().equals("1695966745@qq.com")) {
        SendEmail.sendWholeEmail(user.getEmail(), "您的账户即将注销", "尊敬的"+user.getUsername()+"，您的飞跃账户已太久未登录，还有"+CoreController.coreController.infoService.getByDescription("账号删除缓冲时长文本").getContent()+"将被被注销，请前往官网 https://www.mjczy.life/leapremote.html 下载登录");
        //}
    }

    public void deleteUser() {
        //if(user.getEmail().equals("1695966745@qq.com")) {
        SendEmail.sendWholeEmail(user.getEmail(), "您的账户已注销", "尊敬的"+user.getUsername()+"，您的飞跃账户已被注销，请前往官网 https://www.mjczy.top/leapremote.html 下载注册");
        //}
        UserController.userController.userService.delete(user.getUserId());
    }
}
