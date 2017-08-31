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
package crawler.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Configuration
public class EmailTemplates {

    @Bean
    @Qualifier("email.templates.confirmation")
    public String getConfirmEmailLetterTemplate() {
        return getTemplate("reg_letter.html");
    }

    @Bean
    @Qualifier("email.templates.delivery")
    public String getKeyDeliveryLetterTemplate() {
        return getTemplate("key_letter.html");
    }

    private String getTemplate(String filename) {
   
        StringBuilder result = new StringBuilder();
        try (InputStream iS = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(iS));

            String l = null;
            while ((l = br.readLine()) != null) {
                result.append(l);
            }
        } catch (IOException ex) {
            Logger.getLogger(EmailTemplates.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.toString();
    }
}
