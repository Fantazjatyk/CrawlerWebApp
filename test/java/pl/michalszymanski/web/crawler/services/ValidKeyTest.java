/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import pl.michalszymanski.web.crawler.model.ValidKey;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class ValidKeyTest {

    @ValidKey
    String key;

    public ValidKeyTest(String key) {
        this.key = key;
    }

}
