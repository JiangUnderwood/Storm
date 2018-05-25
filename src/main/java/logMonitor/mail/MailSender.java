package logMonitor.mail;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @Author : Frank Jiang
 * @Date : 22/05/2018 5:43 PM
 */
public class MailSender {
    private static final Logger logger = Logger.getLogger(MailSender.class);

    //发送邮件-邮件内容为文本格式
    public static boolean sendMail(MailInfo mailInfo) {
        try {
            Message mailMessage = generateBaseInfo(mailInfo);
            //设置邮件消息的主要内容
            mailMessage.setText(mailInfo.getMailContent());
            Transport.send(mailMessage);
            logger.info("【 TEXT 邮件发送完毕，成功时间：" + System.currentTimeMillis() + " 】");
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }

    //邮件内容为HTML格式
    public static boolean sendHTMLMail(MailInfo mailInfo) {
        try {
            Message mailMessage = generateBaseInfo(mailInfo);
            //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart part = new MimeMultipart();
            //创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            //设置HTML内容
            html.setContent(mailInfo.getMailContent(), "text/html; charset=utf-8");
            part.addBodyPart(html);
            //将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(part);
            //发送邮件
            Transport.send(mailMessage);
            logger.info("【 HTML 邮件发送完毕，成功时间：" + System.currentTimeMillis() + " 】");
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Message generateBaseInfo(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException {
        //判断是否需要身份认证
        MailAuthenticator auth = null;
        Properties pro = mailInfo.getProperties();
        //如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isAuthValidate()) {
            auth = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getUserPassword());
        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session mailSession = Session.getDefaultInstance(pro, auth);
        Message mailMessage = null;
        //根据session创建一个邮件消息
        mailMessage = new MimeMessage(mailSession);
        //创建邮件发送者地址
        Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromUserName());
        //设置邮件消息的发送者
        mailMessage.setFrom(from);
        //设置邮件的接收者信息
        if (mailInfo.getToAddress() != null) {
            if (mailInfo.getToAddress().contains(",")) {
                //Message.RecipientType.TO属性表示接受者的类型为TO
                mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));
            } else {
                //创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(mailInfo.getToAddress());
                mailMessage.setRecipient(Message.RecipientType.TO, to);
            }
        }
        //设置邮件的抄送者信息
        if (StringUtils.isNotBlank(mailInfo.getCcAddress())) {
            if (mailInfo.getCcAddress().contains(",")) {
                //Message.RecipientType.CC属性表示接受者的类型为CC
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCcAddress()));
            } else {
                //创建邮件的抄送者地址，并设置到邮件消息中
                Address cc = new InternetAddress(mailInfo.getCcAddress());
                mailMessage.setRecipient(Message.RecipientType.CC, cc);
            }
        }
        mailMessage.setSubject(mailInfo.getMailSubject());
        mailMessage.setSentDate(new Date());
        return mailMessage;
    }

}
