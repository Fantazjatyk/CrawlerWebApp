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

import crawler.dao.EmailsHolder;
import crawler.dao.KeysHolder;
import crawler.exceptions.UnknownEmailException;
import java.text.MessageFormat;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

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
