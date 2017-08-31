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
import crawler.dao.EmailsHolder;
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


    @Test
    public void testRegisterSuccess() throws Exception {
        mvc.perform(get("/register/success"))
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));

    }


    @Test
    public void testRegisterFailure() throws Exception {
        mvc.perform(get("/register/failure"))
                .andExpect(status().isOk())
                .andExpect(view().name("template"))
                .andExpect(model().attributeExists("message", "title", "site"));
    }


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
