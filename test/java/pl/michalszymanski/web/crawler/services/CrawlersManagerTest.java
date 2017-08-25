/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import crawler.exception.InitialConfigurationException;
import java.util.Arrays;
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
import pl.michalszymanski.web.crawler.model.CrawlerInit;
import pl.michalszymanski.web.crawler.model.CrawlerResult;

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

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of initCrawler method, of class CrawlersManager.
     */
    @Test
    public void testRunCrawler() {
        String target = "http://www.google.pl";
        String[] sentences = new String[]{"first", "second", "third"};
        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setSentences(sentences);
        init.setTimeLimit(1);
        init.setUrl(target);

        CrawlerResult r = manager.runCrawler(init, email);
        assertFalse(r.getImages().isEmpty());
        assertTrue(r.getTime() > 0);
        assertEquals(r.getTarget(), target);
        assertTrue(Arrays.equals(sentences, r.getSentencesInit()));

    }

    @Test(expected = InitialConfigurationException.class)
    public void testRunCrawler_FAIL() {
        String target = "/www.google.pl";
        String[] sentences = new String[]{"first", "second", "third"};
        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setSentences(sentences);
        init.setTimeLimit(1);
        init.setUrl(target);

        CrawlerResult r = manager.runCrawler(init, email);
    }

    /**
     * Test of getResponse method, of class CrawlersManager.
     */
    @Test
    public void testGetResponse() {
    }

}
