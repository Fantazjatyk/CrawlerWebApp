/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.michalszymanski.web.crawler.dao.EmailsHolder;
import pl.michalszymanski.web.crawler.services.EmailRegisterer;

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
