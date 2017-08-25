/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Service
public class EmailDeliverer {

    @Autowired
    @Qualifier("mail")
    Properties p;

    private Session getEmailSession() {
        return Session.getDefaultInstance(p);
    }

    private Message getMessage(Session s, String recipient, String content, String subject) throws AddressException, MessagingException {
        Message m = new MimeMessage(s);
        m.setFrom(new InternetAddress(p.getProperty("mail.from")));
        m.setContent(content, "text/html");
        m.setSubject(subject);
        m.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        return m;
    }

    public void send(String recipient, String content, String subject) {
        Session s = getEmailSession();
        Message m = null;
        try {
            m = getMessage(s, recipient, content, subject);
            Transport t = s.getTransport("smtp");
            t.connect(p.getProperty("mail.host"), p.getProperty("mail.username"), p.getProperty("mail.password"));
            t.sendMessage(m, m.getAllRecipients());
            t.close();
        } catch (MessagingException ex) {
            Logger.getLogger(EmailDeliverer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
