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
package crawler.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Repository
public class EmailsHolder {

    @Autowired
    NamedParameterJdbcTemplate template;

    SimpleJdbcInsert insert;

    @Autowired
    DataSource ds;

    @PostConstruct
    public void setup() {
        insert = new SimpleJdbcInsert(ds);
        insert.usingColumns("email", "confirmation_key");
        insert.usingGeneratedKeyColumns("id");
        insert.withTableName("unconfirmed_emails");
        insert.compile();
    }

    public Number addEmail(String email) {
        Map map = new HashMap();
        map.put("email", email);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        map.put("confirmation_key", key);
        return insert.executeAndReturnKey(map);
    }

    public void removeEmail(String email) {
        Map map = Collections.singletonMap("email", email);
        template.update("delete from unconfirmed_emails where email = :email", map);
    }

    public String getEmailByConfirmKey(String key) {
        Map map = Collections.singletonMap("key", key);
        return template.queryForObject("select email from unconfirmed_emails where confirmation_key = :key", map, String.class);
    }

    public String getEmailById(Number id) {
        Map map = Collections.singletonMap("id", id);
        return template.queryForObject("select email from unconfirmed_emails where id = :id", map, String.class);
    }

    public String getConfirmationKey(String email) throws EmptyResultDataAccessException {
        Map map = Collections.singletonMap("email", email);
        return template.queryForObject("select confirmation_key from unconfirmed_emails where email = :email", map, String.class);
    }

    public boolean containsEmail(String email) {
        Map map = Collections.singletonMap("email", email);
        String statement = "select count(email) from (select email from unconfirmed_emails union select email from access_keys) as mix where email = :email";

        Long count = template.queryForObject(statement, map, Long.class);
        return count > 0;
    }
}
