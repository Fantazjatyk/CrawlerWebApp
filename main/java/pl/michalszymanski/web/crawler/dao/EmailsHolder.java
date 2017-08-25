/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.dao;

import java.util.HashMap;
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
        HashMap map = new HashMap();
        map.put("email", email);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        map.put("confirmation_key", key);
        return insert.executeAndReturnKey(map);
    }

    public void removeEmail(String email) {
        HashMap map = new HashMap();
        map.put("email", email);
        template.update("delete from unconfirmed_emails where email = :email", map);

    }

    public String getEmailByConfirmKey(String key) {
        HashMap map = new HashMap();
        map.put("key", key);

        return template.queryForObject("select email from unconfirmed_emails where confirmation_key = :key", map, String.class);
    }

    public String getEmailById(Number id) {
        HashMap map = new HashMap();
        map.put("id", id);

        return template.queryForObject("select email from unconfirmed_emails where id = :id", map, String.class);
    }

    public String getConfirmationKey(String email) throws EmptyResultDataAccessException {
        HashMap map = new HashMap();
        map.put("email", email);
        return template.queryForObject("select confirmation_key from unconfirmed_emails where email = :email", map, String.class);
    }

    public boolean containsEmail(String email) {
        HashMap map = new HashMap();
        map.put("email", email);
        String statement = "select count(email) from (select email from unconfirmed_emails union select email from access_keys) as mix where email = :email";

        Long count = template.queryForObject(statement, map, Long.class);
        return count > 0;
    }
}
