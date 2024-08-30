package com.mj.leapremote.util;


import com.mj.leapremote.controller.CoreController;
import com.mj.leapremote.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

public class HttpUtils {
    public static void doDownload(HttpServletResponse response , File file) {
        doDownload(response, file, file.getName());
    }

    public static void doDownload(HttpServletResponse response, File file, String fileName) {
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
            response.addHeader("Content-Length", "" + file.length());
            byte[] buffer = new byte[Integer.parseInt(CoreController.coreController.infoService.getByDescription("单块下载大小").getContent())];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setCookies(HttpServletRequest request, HttpServletResponse response, User user) {
        setCookies(request, response, user, null);
    }

    public static void setCookies(HttpServletRequest request, HttpServletResponse response, User user, String domain) {
        Cookie cookie = new Cookie("username", Utils.encodeString(user.getUsername()));
        if(domain!=null)
            cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.parseInt(CoreController.coreController.infoService.getByDescription("Cookie时长").getContent()));
        response.addCookie(cookie);
        cookie = new Cookie("password", Utils.encodeString(user.getPassword()));
        if(domain!=null)
            cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.parseInt(CoreController.coreController.infoService.getByDescription("Cookie时长").getContent()));
        response.addCookie(cookie);
        request.getSession().setAttribute("user", user);
    }
}
