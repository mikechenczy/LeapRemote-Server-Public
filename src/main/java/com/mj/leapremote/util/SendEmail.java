package com.mj.leapremote.util;

import com.mj.leapremote.controller.CoreController;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {
    public static void sendEmail(String mail, String check) {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", CoreController.coreController.infoService.getByDescription("官方邮箱服务器地址").getContent()); //// 设置QQ邮件服务器
            prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
            prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

            // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
            if(CoreController.coreController.infoService.getByDescription("是否SSL加密").getContent().equals("true")) {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                prop.put("mail.smtp.ssl.enable", "true");
                prop.put("mail.smtp.ssl.socketFactory", sf);
            }

            //使用JavaMail发送邮件的5个步骤

            //创建定义整个应用程序所需的环境信息的 Session 对象

            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //发件人邮件用户名、授权码
                    return new PasswordAuthentication(CoreController.coreController.infoService.getByDescription("官方邮箱").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱密码").getContent());
                }
            });


            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(false);

            //2、通过session得到transport对象
            Transport ts = session.getTransport();

            //3、使用邮箱的用户名和授权码连上邮件服务器
            ts.connect(CoreController.coreController.infoService.getByDescription("官方邮箱服务器地址").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱密码").getContent());

            //4、创建邮件

            //创建邮件对象
            MimeMessage message = new MimeMessage(session);

            //指明邮件的发件人
            message.setFrom(new InternetAddress(CoreController.coreController.infoService.getByDescription("官方邮箱").getContent()));

            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发//To sb.
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    mail));

            //邮件的标题
            message.setSubject(CoreController.coreController.infoService.getByDescription("应用名称").getContent()+"验证码");

            //邮件的文本内容
            message.setContent("欢迎使用"+CoreController.coreController.infoService.getByDescription("应用名称").getContent()+"，您的账号验证码是：" + check + "\n此验证码在15分钟内有效", "text/html;charset=UTF-8");

            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());

            ts.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void sendWholeEmail(String mail, String subject, String content) {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", CoreController.coreController.infoService.getByDescription("官方邮箱服务器地址").getContent()); //// 设置QQ邮件服务器
            prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
            prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

            // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
            if(CoreController.coreController.infoService.getByDescription("是否SSL加密").getContent().equals("true")) {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                prop.put("mail.smtp.ssl.enable", "true");
                prop.put("mail.smtp.ssl.socketFactory", sf);
            }

            //使用JavaMail发送邮件的5个步骤

            //创建定义整个应用程序所需的环境信息的 Session 对象

            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //发件人邮件用户名、授权码
                    return new PasswordAuthentication(CoreController.coreController.infoService.getByDescription("官方邮箱").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱密码").getContent());
                }
            });


            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(false);

            //2、通过session得到transport对象
            Transport ts = session.getTransport();

            //3、使用邮箱的用户名和授权码连上邮件服务器
            ts.connect(CoreController.coreController.infoService.getByDescription("官方邮箱服务器地址").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱").getContent(), CoreController.coreController.infoService.getByDescription("官方邮箱密码").getContent());

            //4、创建邮件

            //创建邮件对象
            MimeMessage message = new MimeMessage(session);

            //指明邮件的发件人
            message.setFrom(new InternetAddress(CoreController.coreController.infoService.getByDescription("官方邮箱").getContent()));

            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发//To sb.
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    mail));

            //邮件的标题
            message.setSubject(subject);

            //邮件的文本内容
            message.setContent(content, "text/html;charset=UTF-8");

            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());

            ts.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
