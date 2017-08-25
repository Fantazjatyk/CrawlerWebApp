/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import java.util.Arrays;
import javax.transaction.Transactional;
import pl.michalszymanski.web.crawler.Conf;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import pl.michalszymanski.web.crawler.model.CrawlerInit;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
@Transactional
public class ViewResolverTest {

    public ViewResolverTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    MockMvc mvc;

    @Autowired
    WebApplicationContext ctx;

    @Before
    public void setUp() {
        for (String s : ctx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    /**
     * Test of welcome method, of class ViewResolver.
     */
    @Test
    public void testWelcome() throws Exception {

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("site"));

    }

    /**
     * Test of results method, of class ViewResolver.
     */
    @Test
    public void testResults() throws Exception {
        mvc.perform(get("/results"))
                .andExpect(status().isOk())
                .andExpect(view().name("results"));
    }

    /**
     * Test of registration method, of class ViewResolver.
     */
    @Test
    public void testRegistration() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("site"));
    }

}
