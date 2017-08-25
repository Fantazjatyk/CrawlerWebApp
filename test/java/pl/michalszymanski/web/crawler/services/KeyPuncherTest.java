/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import javax.transaction.Transactional;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
@Transactional
public class KeyPuncherTest {

    public KeyPuncherTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @Autowired
    KeyPuncher kp;

    @Autowired
    KeysHolder kh;

    String email = "email@email.com";

    /**
     * Test of useKey method, of class KeyPuncher.
     */
    @Test
    public void testUseKey() {
        String key = kh.registerKey(email);

        Key wrapper = kh.getKeyWrapper(key);
        kp.useKey(key);
        kp.useKey(key);
        kp.useKey(key);

        Key wrapper2 = kh.getKeyWrapper(key);
        assertEquals(wrapper.getRemainedUse() - 3, wrapper2.getRemainedUse());

    }

    /**
     * Test of useKey method, of class KeyPuncher.
     */
}
