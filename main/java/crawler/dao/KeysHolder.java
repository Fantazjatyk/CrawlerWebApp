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

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import crawler.dao.mappers.KeyMapper;
import crawler.model.Key;

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
