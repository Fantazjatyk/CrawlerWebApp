/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.michalszymanski.web.crawler.model.ValidURL;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
public class URLValidatorTest {

    public URLValidatorTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    Validator valid = Validation.buildDefaultValidatorFactory().getValidator();

    @Before
    public void setUp() {
        validObject = new TestClass("http://www.google.pl");
        invalidObject = new TestClass(":dfdsf;fd");
    }

    TestClass validObject;
    TestClass invalidObject;

    /**
     * Test of isValid method, of class URLValidator.
     */
    @Test
    public void testIsValid() {
        Set<ConstraintViolation<TestClass>> set = valid.validate(validObject);
        assertTrue(set.isEmpty());
    }

    /**
     * Test of initialize method, of class URLValidator.
     */
    @Test
    public void testIsInValid() {
        Set<ConstraintViolation<TestClass>> set = valid.validate(invalidObject);
        assertTrue(set.size() == 1);
    }

}
