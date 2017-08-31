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
package crawler.services;

import crawler.dao.KeysHolder;
import crawler.exception.InitialConfigurationException;
import crawler.model.CrawlerInit;
import crawler.model.CrawlerResult;
import java.util.Arrays;
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
public class CrawlersManagerTest {

    String email = "email@email.com";

    @Autowired
    CrawlersManager manager;

    @Autowired
    KeysHolder kh;

    String key;

    @Before
    public void setUp() {
        key = kh.registerKey(email);
    }

    public CrawlersManagerTest() {
    }

    @Test
    public void testRunCrawler() {
        String target = "http://www.google.pl";
        String[] sentences = new String[]{"first", "second", "third"};
        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setSentences(sentences);
        init.setTimeLimit(1);
        init.setUrl(target);
        init.setLookForImages(true);

        CrawlerResult r = manager.runCrawler(init, email);
        assertFalse(r.getImages() == null || r.getImages().isEmpty());
        assertTrue(r.getTime() > 0);
        assertEquals(r.getTarget(), target);
        assertTrue(Arrays.equals(sentences, r.getSentencesInit()));
    }

    @Test(expected = InitialConfigurationException.class)
    public void testRunCrawler_BadConfiguration() {
        String target = "/www.google.pl";
        String[] sentences = new String[]{"first", "second", "third"};
        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setSentences(sentences);
        init.setTimeLimit(1);
        init.setUrl(target);

        manager.runCrawler(init, email);
    }

}
