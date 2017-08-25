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
import org.springframework.dao.EmptyResultDataAccessException;
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
@Transactional
@WebAppConfiguration
public class EmailsHolderTest {

    public EmailsHolderTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of setup method, of class EmailsHolder.
     */
    @Test
    public void testSetup() {
    }

    /**
     * Test of addEmail method, of class EmailsHolder.
     */
    String email = "email@email.com";

    @Autowired
    EmailsHolder eh;

    Number id;

    public void testAddEmail() {
        Number id = eh.addEmail(email);
        assertNotNull(eh.getEmailById(id));
    }

    /**
     * Test of removeEmail method, of class EmailsHolder.
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void testRemoveEmail() {
        Number id = eh.addEmail(email);
        assertNotNull(eh.getEmailById(id));
        eh.removeEmail(email);
        eh.getEmailById(id);

    }

    /**
     * Test of getEmailByConfirmKey method, of class EmailsHolder.
     */
    @Test
    public void testGetByConfirmKey() {
        Number id = eh.addEmail(email);
        String key = eh.getConfirmationKey(email);
        String email2 = eh.getEmailByConfirmKey(key);
        assertEquals(email, email2);
    }

    /**
     * Test of getConfirmationKey method, of class EmailsHolder.
     */
    @Test
    public void testGetConfirmationKey() {
        Number id = eh.addEmail(email);
        String key = eh.getConfirmationKey(email);
        assertFalse(key.isEmpty());
    }

    /**
     * Test of containsEmail method, of class EmailsHolder.
     */
    @Test
    public void testContainsEmail() {
        Number id = eh.addEmail(email);
        assertTrue(eh.containsEmail(email));
    }

}
