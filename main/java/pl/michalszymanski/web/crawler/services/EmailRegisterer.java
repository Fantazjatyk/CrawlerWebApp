/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import pl.michalszymanski.web.crawler.dao.EmailsHolder;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.exceptions.UnknownEmailException;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Service
public class EmailRegisterer {

    @Autowired
    EmailsHolder eh;

    @Autowired
    EmailDeliverer ed;

    @Autowired
    @Qualifier("mail")
    Properties p;

    @Autowired
    @Qualifier("email.templates.delivery")
    String deliveryTemplate;

    @Autowired
    @Qualifier("email.templates.confirmation")
    String confirmationTemplate;

    public void sendConfirmationLetter(String email) {
        if (!eh.containsEmail(email)) {
            throw new UnknownEmailException();
        }
        String validationKey = eh.getConfirmationKey(email);
        String content = confirmationTemplate;

        String confirmUrl = p.getProperty("mail.confirmation.url") + validationKey;
        content = MessageFormat.format(content, confirmUrl);
        ed.send(email, content, "Confirm your email address");
    }

    public void registerEmail(String email) {
        eh.addEmail(email);
        this.sendConfirmationLetter(email);
    }

    @Autowired
    KeysHolder kh;

    public void confirmEmail(String confirmKey) throws EmptyResultDataAccessException {
        String email = eh.getEmailByConfirmKey(confirmKey);
        eh.removeEmail(email);
        String k = kh.registerKey(email);
        String content = deliveryTemplate;
        content = MessageFormat.format(content, k);

        ed.send(email, content, "Your crawler key");
    }

}
