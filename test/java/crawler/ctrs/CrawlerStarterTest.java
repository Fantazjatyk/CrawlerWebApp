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
package crawler.ctrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import crawler.dao.KeysHolder;
import crawler.model.CrawlerInit;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
public class CrawlerStarterTest {

    MockMvc mvc;

    @Autowired
    WebApplicationContext ctx;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

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

        mvc.perform(post("/crawl").content(serialized).contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8));

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
