/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import java.util.Random;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.ValidKey;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
@Transactional
public class KeyValidatorTest {

    public KeyValidatorTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    final String email = "email@email.com";

    @Autowired
    KeysHolder kh;

    @Before
    public void setUp() {

    }
    @Autowired
    @Qualifier("validator")
    Validator valid;

    /**
     * Test of initialize method, of class KeyValidator.
     */
    /**
     * Test of isValid method, of class KeyValidator.
     */
    @Test
    public void testIsValid() {
        String key = kh.registerKey(email);
        Set set = valid.validate(new ValidKeyTest(key));
        assertTrue(set.isEmpty());
    }

    class TestClass {

    }

    @Test
    public void testIsInValid() {

        Set set = valid.validate(new ValidKeyTest(Long.toString(System.currentTimeMillis())));
        assertFalse(set.isEmpty());
    }

}
