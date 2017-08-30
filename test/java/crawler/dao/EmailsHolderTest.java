/* 
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package crawler.dao;

import crawler.dao.EmailsHolder;
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
import crawler.model.Key;

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
