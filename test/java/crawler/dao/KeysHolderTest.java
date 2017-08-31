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

import crawler.model.Key;
import javax.transaction.Transactional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

    String key;
    String email = "email@email.com";

    @Before
    public void setUp() {
        key = kh.registerKey(email);

    }

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
