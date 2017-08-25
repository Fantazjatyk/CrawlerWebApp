/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.dao;

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
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
@Transactional
public class KeysHolderTest {

    public KeysHolderTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    String key;
    String email = "email@email.com";

    @Before
    public void setUp() {
        key = kh.registerKey(email);

    }

    /**
     * Test of getKeyWrapper method, of class KeysHolder.
     */
    @Autowired
    KeysHolder kh;

    @Test
    public void testRegisterAndGetKeyWrapper() {
        Key wrapper = kh.getKeyWrapper(key);
        assertNotNull(wrapper);
        assertNotNull(wrapper.getEmail());
        assertNotNull(wrapper.getValue());
        assertFalse(wrapper.getEmail().isEmpty());
        assertFalse(wrapper.getValue().isEmpty());
        assertTrue(wrapper.getRemainedUse() > 0);
    }

    /**
     * Test of registerKey method, of class KeysHolder.
     */
    /**
     * Test of getPublicKeyWrapper method, of class KeysHolder.
     */
    @Test
    public void testGetPublicKeyWrapper() {
        kh.refreshAllKeys();
        Key publicKey = kh.getPublicKeyWrapper();
        assertNotNull(publicKey);
        assertNull(publicKey.getEmail());
        assertNotNull(publicKey.getValue());
        assertFalse(publicKey.getValue().isEmpty());
        assertTrue(publicKey.getRemainedUse() > 0);
    }

    /**
     * Test of refreshAllKeys method, of class KeysHolder.
     */
    @Test
    public void testRefreshAllKeys() {
        Key wrapper = kh.getKeyWrapper(key);
        kh.deicrementKeyUsage(wrapper);
        kh.deicrementKeyUsage(wrapper);
        kh.deicrementKeyUsage(wrapper);

        Key wrapper2 = kh.getKeyWrapper(key);
        assertEquals(wrapper.getRemainedUse() - 3, wrapper2.getRemainedUse());

        kh.refreshAllKeys();

        Key wrapper3 = kh.getKeyWrapper(key);
        assertEquals(wrapper.getRemainedUse(), wrapper3.getRemainedUse());
    }

    /**
     * Test of deicrementKeyUsage method, of class KeysHolder.
     */
    @Test
    public void testDeicrementKeyUsage() {
        Key wrapper = kh.getKeyWrapper(key);
        kh.deicrementKeyUsage(wrapper);
        kh.deicrementKeyUsage(wrapper);
        kh.deicrementKeyUsage(wrapper);

        Key wrapper2 = kh.getKeyWrapper(key);
        assertEquals(wrapper.getRemainedUse() - 3, wrapper2.getRemainedUse());
    }

}
