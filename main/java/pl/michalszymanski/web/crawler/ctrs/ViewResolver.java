/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Controller
public class ViewResolver {

    @Autowired
    KeysHolder kh;

    @RequestMapping("/")
    public String welcome(Model model) {
        Key publicKey = kh.getPublicKeyWrapper();
        if (publicKey.getRemainedUse() > 0) {
            model.addAttribute("key", publicKey.getValue());
        }
        model.addAttribute("site", "home");
        return "template";
    }

    @RequestMapping("/results")
    public String results() {
        return "results";
    }

    @RequestMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("site", "registration");
        return "template";
    }


}
