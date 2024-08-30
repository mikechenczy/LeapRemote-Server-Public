package com.mj.leapremote;

import com.mj.leapremote.controller.CoreController;
import com.mj.leapremote.controller.UserController;
import com.mj.leapremote.model.UserData;
import com.mj.leapremote.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mike_Chen
 * @date 2023/6/24
 * @apiNote
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //new Server(Define.port).start();
        new Thread(() -> {
            for(UserData userData : UserController.userController.userDataService.getAll()) {
                userData.setControlId(0);
                UserController.userController.userDataService.update(userData);
            }
            try {
                Thread.sleep(Utils.getTomorrowZeroMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (CoreController.coreController.signInService) {
                CoreController.coreController.signInService.deleteAll();
            }
            synchronized (CoreController.coreController.loginInfoService) {
                CoreController.coreController.loginInfoService.deleteAll();
            }
            while (true) {
                try {
                    Thread.sleep(1000);//Make sure tomorrow zero millis correct.
                    Thread.sleep(Utils.getTomorrowZeroMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (CoreController.coreController.signInService) {
                    CoreController.coreController.signInService.deleteAll();
                }
                synchronized (CoreController.coreController.loginInfoService) {
                    CoreController.coreController.loginInfoService.deleteAll();
                }
            }
        }).start();
    }
}
