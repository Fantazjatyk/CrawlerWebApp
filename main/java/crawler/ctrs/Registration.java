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

import crawler.dao.EmailsHolder;
import crawler.services.EmailRegisterer;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Controller
public class Registration {

    @Autowired
    ViewResolver vr;

    @Autowired
    EmailRegisterer er;

    @Autowired
    EmailsHolder eh;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String email, Model model) {
        if (eh.containsEmail(email)) {
            return "forward:/register/failure";
        } else {
            er.registerEmail(email);
            return "forward:/register/success";
        }

    }

    @RequestMapping("/register/success")
    public String registerSuccess(Model model) {
        model.addAttribute("message", "The key will be sent to you in few minutes");
        model.addAttribute("title", "Your registration success");
        model.addAttribute("site", "registration_status");
        return "template";
    }

    @RequestMapping("/register/failure")
    public String registerFailure(Model model) {
        model.addAttribute("message", "That email already exists in our databases");
        model.addAttribute("title", "Your registration failed");
        model.addAttribute("site", "registration_status");
        return "template";
    }

    @RequestMapping("/confirmemail")
    public String confirmEmail(@RequestParam String id, Model model, HttpServletResponse rs) {

        try{
        er.confirmEmail(id);
        }
        catch(EmptyResultDataAccessException e){
            rs.setStatus(405);
        }
        model.addAttribute("message", "You just confirmed your email.");
        model.addAttribute("title", "Hurray!");
        model.addAttribute("site", "registration_status");
        return "template";
    }
}
