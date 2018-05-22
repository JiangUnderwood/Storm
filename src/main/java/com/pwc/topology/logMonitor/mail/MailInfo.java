package com.pwc.topology.logMonitor.mail;

import java.util.List;
import java.util.Properties;

/**
 * @Author : Frank Jiang
 * @Date : 22/05/2018 4:11 PM
 */
public class MailInfo {
    private String mailServerHost;  //发送邮件的服务器IP
    private String mailServerPort;  //发送邮件的服务器端口
    private String userName;        //邮件发送服务器的登录用户
    private String userPassword;    //邮件发送服务器的登录密码
    private String fromAddress;     //邮件发送者的地址
    private String toAddress;       //邮件接收者的地址
    private String ccAddress;       //邮件抄送者的地址
    private String fromUserName = "日志监控平台";     //邮件发送者的名称，显示在他人邮件的发送人
    private String mailSubject;     //邮件主题
    private String mailContent;     //邮件的文本内容
    private boolean authValidate = true;    //是否需要身份验证
    private Properties properties;   //邮件会话属性

    public MailInfo() {

    }

    public MailInfo(String title, String content, List<String> receiver, List<String> ccList) {
        this.mailServerHost = MailCenterConstant.SMTP_SERVER;
        this.userName = MailCenterConstant.USER;
        this.userPassword = MailCenterConstant.PWD;
        this.fromAddress = MailCenterConstant.FROM_ADDRESS;
        this.toAddress = listToStringFormat(receiver);
        this.ccAddress = ccList == null ? "" : listToStringFormat(ccList);
        this.mailSubject = title;
        this.mailContent = content;
    }

    private synchronized String listToStringFormat(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                builder.append(list.get(i));
            } else {
                builder.append(list.get(i)).append(",");
            }
        }
        return builder.toString();
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public boolean isAuthValidate() {
        return authValidate;
    }

    public void setAuthValidate(boolean authValidate) {
        this.authValidate = authValidate;
    }

    public Properties getProperties() {
        Properties pro = new Properties();
        pro.put("mail.smtp.host", this.mailServerHost);
        pro.put("mail.smtp.port", this.mailServerPort);
        pro.put("mail.smtp.auth", authValidate ? "true" : "false");
        pro.put("mail.smtp.starttls.enable", "true");
        return pro;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
