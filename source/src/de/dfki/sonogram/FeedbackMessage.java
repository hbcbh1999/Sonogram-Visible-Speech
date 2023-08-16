package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.*;

// ---------------------------------------------------------------------------------------------------------------------------------

public class FeedbackMessage extends JDialog {

  Sonogram reftomain;
  JTextField from = new JTextField(1);
  JTextArea messageText = new JTextArea(16, 80);

  // ---------------------------------------------------------------------------------------------------------------------------------

  public FeedbackMessage(Sonogram ref) {
    super(ref, "Feedback Message Box", true);
    reftomain = ref;
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    final JLabel label = new JLabel("Your E-Mail Adress, so I can Answer You:");
    from.setBorder(new RoundedCornerBorder());
    JPanel email = new JPanel();
    email.setLayout(new BorderLayout());
    email.setBorder(new TitledBorder(new EtchedBorder(), "Who you are ?"));
    email.add(label, BorderLayout.WEST);
    email.add(from, BorderLayout.CENTER);
    email.setToolTipText(
        "<html>You do not need to advise you eMail adress<br>to send this message, but if you do so"
            + " i can<br>give you a feedback message.");

    JPanel message = new JPanel();
    message.setBorder(
        new TitledBorder(new EtchedBorder(), "What you would like to say about Sonogram ?"));
    messageText.setFont(new Font("Monospaced", Font.PLAIN, 12));
    messageText.setText(
        "INSERT YOUR TEXT HERE:\n\n"
            + "For Example:\n"
            + "I use Sonogram mainly for...\n"
            + "I have a great idea, could you please implement the feature...\n"
            + "My opinion about Sonogram...\n"
            + "I found a Bug in...");
    JScrollPane scrollpane = new JScrollPane(messageText);
    message.add(scrollpane);
    scrollpane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
    scrollpane.getHorizontalScrollBar().setPreferredSize(new Dimension(12, 0));

    JButton send =
        new JButton("Send Message", new ImageIcon(Sonogram.class.getResource("SendMail.gif")));
    ActionListener sendListener =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String emailText = new String("Feedback Message from Sonogram User\n");
            emailText += "SENDER EMAIL ADRESS: ";
            emailText += from.getText() + "\n";
            emailText += "MESSAGE TEXT:\n";
            emailText += messageText.getText();
            emailText += "\n" + getSystemProperties();
            emailText += "StartupTime: " + reftomain.startupSeconds + " Seconds";
            sendText(emailText);
            setVisible(false);
            dispose();
          }
        };
    send.addActionListener(sendListener);
    send.setMargin(new Insets(0, 0, 0, 0));
    JPanel p = new JPanel();
    p.setBorder(
        new TitledBorder(
            new EtchedBorder(), "Please provide me with a Bug report or new Ideas..."));
    p.setLayout(new BorderLayout());
    p.add(email, BorderLayout.NORTH);
    p.add(message, BorderLayout.CENTER);
    getContentPane().add(p, "Center");
    getContentPane().add(send, "South");
    pack();
    setLocationRelativeTo(null);
    setResizable(false);
    setVisible(true);
  }

  // ---------------------------------------------------------------------------------------------------------------------------------

  public void sendText(String text) {
    System.out.println("--> Try to send the Message.");
    SendEmail mailSender = new SendEmail();
    if (mailSender.sentEmailMessage(text) == true) {
      JOptionPane.showMessageDialog(
          reftomain,
          "<html>Your message <u>has been sent</u>. We will contact you shortly if your<br>message"
              + " requires a response, and you provided an email address.",
          "Message successfully Sent...",
          1,
          new ImageIcon(Sonogram.class.getResource("chris.jpg")));
      System.out.println("--> Feedback Message successfully send.");
    } else {
      int confirm =
          JOptionPane.showOptionDialog(
              reftomain,
              "There was a problem sending your Feedback Message to me.\n"
                  + "This could be a Problem with your Internet Connection or your\n"
                  + "Firewall. Would you like to try it again ?",
              "Feedback Message could not been Send !",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.ERROR_MESSAGE,
              null,
              null,
              null);
      if (confirm == 0) { // send again
        sendText(text);
      }
      System.out.println("--> PROBLEM while sending Feedback Message.");
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------------------

  public String getSystemProperties() {
    String propertiesString = new String("\nSYSTEM PROPERTIES:\n");
    propertiesString += "Sonogram Version = " + reftomain.version + "\n";
    propertiesString += "Sonogram Build = " + reftomain.build + "\n";
    Properties sysprops = System.getProperties();
    java.util.Enumeration propnames = sysprops.propertyNames();
    while (propnames.hasMoreElements()) {
      String propname = (String) propnames.nextElement();
      propertiesString += propname + "=" + System.getProperty(propname) + "\n";
    }
    return propertiesString;
  }

  // ---------------------------------------------------------------------------------------------------------------------------------

  class RoundedCornerBorder extends AbstractBorder {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int r = height - 1;
      RoundRectangle2D round = new RoundRectangle2D.Float(x, y, width - 1, height - 1, r, r);
      Container parent = c.getParent();
      if (parent != null) {
        g2.setColor(parent.getBackground());
        Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
        corner.subtract(new Area(round));
        g2.fill(corner);
      }
      g2.setColor(Color.GRAY);
      g2.draw(round);
      g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(4, 8, 4, 8);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      insets.left = insets.right = 8;
      insets.top = insets.bottom = 4;
      return insets;
    }
  }
}
