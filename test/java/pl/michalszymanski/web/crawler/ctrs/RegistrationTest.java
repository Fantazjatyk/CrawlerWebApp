/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.michalszymanski.web.crawler.dao.EmailsHolder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/basic.xml"})
@WebAppConfiguration
@Transactional
public class RegistrationTest {

    public RegistrationTest() {
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

    @Autowired
    EmailsHolder eh;

    String email = "email@email.com";

    /**
     * Test of register method, of class Registration.
     */
    @Test
    public void testRegister_OK() throws Exception {
        mvc.perform(post("/register?email=" + email))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.forwardedUrl("/register/success"));

    }

    @Test
    public void testRegister_FAIL_BadRequest() throws Exception {
        mvc.perform(post("/register"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegister_FAIL_EmailExists() throws Exception {
        eh.addEmail(email);
        mvc.perform(post("/register?email=" + email))
                .andDo(print())
                .andExpect(forwardedUrl("/register/failure"));
    }

    /**
     * Test of registerSuccess method, of class Registration.
     */
    @Test
    public void testRegisterSuccess() throws Exception {
        mvc.perform(get("/register/success"))
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));

    }

    /**
     * Test of registerFailure method, of class Registration.
     */
    @Test
    public void testRegisterFailure() throws Exception {
        mvc.perform(get("/register/failure"))
                .andExpect(status().isOk())
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));
    }

    /**
     * Test of confirmEmail method, of class Registration.
     */
    @Test
    public void testConfirmEmail() throws Exception {
        eh.addEmail(email);
        mvc.perform(get("/confirmemail?id=" + eh.getConfirmationKey(email)))
                .andExpect(status().isOk())
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));
    }

    @Test
    public void testConfirmEmail_FAIL() throws Exception {

        mvc.perform(get("/confirmemail?id=" + Long.toString(System.currentTimeMillis())))
                .andDo(print())
                .andExpect(status().is(405))
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));
    }

}
