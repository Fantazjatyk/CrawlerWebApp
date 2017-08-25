/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Service
public class KeyPuncher {

    @Autowired
    KeysHolder kh;

    public void useKey(Key k) {
        if (k != null && k.getRemainedUse() > 0) {
            kh.deicrementKeyUsage(k);
        }
    }

    public void useKey(String s) {
        Key k = kh.getKeyWrapper(s);
        if (k != null && k.getRemainedUse() > 0) {
            kh.deicrementKeyUsage(k);
        }
    }

}
