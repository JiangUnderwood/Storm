package logMonitor.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Author : Frank Jiang
 * @Date : 22/05/2018 5:34 PM
 */
public class MailAuthenticator extends Authenticator {
    String userName;
    String userPassword;

    public MailAuthenticator() {
        super();
    }

    public MailAuthenticator(String user, String pwd) {
        super();
        this.userName = user;
        this.userPassword = pwd;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, userPassword);
    }
}
