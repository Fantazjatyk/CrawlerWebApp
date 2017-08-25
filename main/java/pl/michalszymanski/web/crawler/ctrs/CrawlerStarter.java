/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.ctrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crawler.exception.InitialConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.CrawlerInit;
import pl.michalszymanski.web.crawler.model.CrawlerResult;
import pl.michalszymanski.web.crawler.model.Key;
import pl.michalszymanski.web.crawler.services.CrawlersManager;
import pl.michalszymanski.web.crawler.services.KeyPuncher;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@RestController
public class CrawlerStarter {

    @Autowired
    Validator validator;

    @Autowired
    CrawlersManager manager;

    @Autowired
    KeysHolder keys;

    @Autowired
    KeyPuncher kp;

    @Autowired
    @Qualifier("specialObjectMapper")
    ObjectMapper mapper;

    @PostMapping(value = "/crawl", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity handleRequest(Model model, @RequestBody CrawlerInit init, HttpServletResponse response, HttpServletRequest rq) throws JsonProcessingException {

        Set<ConstraintViolation<CrawlerInit>> s = validator.validate(init);

        if (!s.isEmpty()) {
            List<String> list = s.stream().map(el -> el.getMessage()).collect(Collectors.toList());
            return new ResponseEntity<List<String>>(list, HttpStatus.BAD_REQUEST);
        }

        kp.useKey(init.getKey());
        CrawlerResult result = manager.runCrawler(init, rq.getSession().getId());

        String json = mapper.writeValueAsString(result);
        return ResponseEntity.ok(json);
    }

}
