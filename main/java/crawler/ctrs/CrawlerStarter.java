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
import crawler.dao.KeysHolder;
import crawler.model.CrawlerInit;
import crawler.model.CrawlerResult;
import crawler.model.Key;
import crawler.services.CrawlersManager;
import crawler.services.KeyPuncher;

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
