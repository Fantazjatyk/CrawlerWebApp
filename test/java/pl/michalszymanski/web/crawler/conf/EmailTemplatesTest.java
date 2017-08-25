/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.conf;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class EmailTemplatesTest {

    public EmailTemplatesTest() {
    }

    EmailTemplates et;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.et = new EmailTemplates();
    }

    /**
     * Test of getConfirmEmailLetterTemplate method, of class EmailTemplates.
     */
    @Test
    public void testGetLetterTemplate() {
        String result = et.getConfirmEmailLetterTemplate();
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testGetKeyDeliveryLetterTemplate() {
        String result = et.getKeyDeliveryLetterTemplate();
        assertNotNull(result);
        System.out.println(result);
    }

}
