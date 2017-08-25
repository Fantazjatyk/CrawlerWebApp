/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Controller
public class ErrorsHandler {

    @RequestMapping("/errors")
    public String handleException(Model model, Exception e, HttpServletResponse rs) {
        int code = rs.getStatus();
        model.addAttribute("error_code", code);
        model.addAttribute("error_desc", HttpStatus.valueOf(code).getReasonPhrase());
        return "errors";
    }
}
