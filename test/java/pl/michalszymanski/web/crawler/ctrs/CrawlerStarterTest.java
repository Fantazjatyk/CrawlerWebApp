/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.CrawlerInit;
import pl.michalszymanski.web.crawler.model.CrawlerResult;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
public class CrawlerStarterTest {

    public CrawlerStarterTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    MockMvc mvc;

    @Autowired
    WebApplicationContext ctx;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    /**
     * Test of handleRequest method, of class CrawlerStarter.
     */
    @Autowired
    KeysHolder kh;

    String email = "email@email.com";

    @Test
    public void testHandleRequest() throws Exception {
        String key = kh.registerKey(email);

        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setTimeLimit(0);
        init.setUrl("http://yahoo.com");
        init.setSentences(new String[]{"dsasad", "Sadsa"});

        String serialized = new ObjectMapper().writeValueAsString(init);

        MvcResult result = mvc.perform(post("/crawl").content(serialized).contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void testHandleRequest_FAIL() throws Exception {
        String key = kh.registerKey(email);

        CrawlerInit init = new CrawlerInit();
        init.setKey(key);
        init.setTimeLimit(0);
        init.setUrl("ht/yahoo.com");
        init.setSentences(new String[]{"dsasad", "Sadsa"});

        String serialized = new ObjectMapper().writeValueAsString(init);

        mvc.perform(post("/crawl").content(serialized).contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8));

    }
}
