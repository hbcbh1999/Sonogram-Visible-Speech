package de.dfki.sonogram;

import java.util.Properties;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

public class SendEmail {
  String SMTP_SERVER = "mail.gmx.net";
  String SMTP_AUTH_USER = "ChristophLauer@gmx.net";
  String SMTP_AUTH_PASS = "SonogramFeedbackPasswort";
  String MAIL_SENDER = "ChristophLauer@gmx.net";
  String MAIL_RECEIVER = "christophlauer@me.com";
  String MAIL_SUBJECT = "Sonogram Feedback";

  public static void main(String[] args) {
    SendEmail mail = new SendEmail();
  }

  public SendEmail() {}

  public boolean sentEmailMessage(String mailText) {
    try {
      SMTPAuthenticator auth = new SMTPAuthenticator();
      Properties props = System.getProperties();
      props.put("mail.smtp.host", SMTP_SERVER);
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      Session session = Session.getDefaultInstance(props, auth);
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(MAIL_SENDER));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_RECEIVER));
      message.setSubject(MAIL_SUBJECT);
      message.setText(mailText);
      Transport.send(message);
    } catch (Exception e) {
      System.out.println("--> ERROR while send the E-Mail message: " + e);
      return (false);
    }
    return (true);
  }

  private class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PASS);
    }
  }
}
