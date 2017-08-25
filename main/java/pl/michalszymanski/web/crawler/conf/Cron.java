/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.conf;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import pl.michalszymanski.web.crawler.dao.KeysHolder;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Configuration
public class Cron {

    @Autowired
    KeysHolder kh;

    @Scheduled(cron = "0 0 12 * * ?")
    public void refreshKeysUsage() {
      kh.refreshAllKeys();
    }
}
