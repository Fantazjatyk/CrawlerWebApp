/* 
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package crawler.services;

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
