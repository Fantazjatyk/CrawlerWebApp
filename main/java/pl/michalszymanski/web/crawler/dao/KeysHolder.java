/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.dao;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.michalszymanski.web.crawler.dao.mappers.KeyMapper;
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Repository
public class KeysHolder {

    @Autowired
    NamedParameterJdbcTemplate template;

    @Autowired
    KeyMapper mapper;

    public Key getKeyWrapper(String value) {
        HashMap params = new HashMap();
        params.put("value", value);
        Key key = null;

        try {
            key = template.queryForObject("select * from access_keys where value = :value", params, mapper);
        } catch (EmptyResultDataAccessException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        return key;
    }

    public String registerKey(String email) {
        HashMap params = new HashMap();
        String key = RandomStringUtils.randomAlphanumeric(8);
        params.put("email", email);
        params.put("value", key);
        params.put("usages_left", 10);
        template.update("insert into access_keys (value, email, usages_left) values (:value, :email, :usages_left)", params);
        return key;
    }

    public Key getPublicKeyWrapper() {
        return this.getKeyWrapper("public_key");
    }

    public void refreshAllKeys() {
        HashMap params = new HashMap();
        params.put("value", 10);
        String st = "update access_keys set usages_left = :value";
        template.update(st, params);
    }

    public void deicrementKeyUsage(Key key) {
        HashMap params = new HashMap();
        params.put("value", key.getValue());
        template.update("update access_keys set usages_left = usages_left -1 where value=:value", params);
    }

}
