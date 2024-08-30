package com.mj.leapremote.util;

import com.mj.leapremote.Define;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Mike_Chen
 * @date 2023/8/19
 * @apiNote
 */
public class PingUtil {
    public static boolean isReachable(String ip) {
        try {
            InetAddress inet6Address = InetAddress.getByName(ip);
            return inet6Address.isReachable(Define.connectTimeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
