/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStream iS = this.getClass().getClassLoader().getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(iS));
        StringBuilder result = new StringBuilder();

        try {
            String l = null;

            while ((l = br.readLine()) != null) {
                result.append(l);
            }
        } catch (IOException e) {
        }
        return result.toString();
    }
}
