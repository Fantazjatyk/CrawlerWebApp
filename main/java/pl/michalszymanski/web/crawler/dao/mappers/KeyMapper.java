/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pl.michalszymanski.web.crawler.model.Key;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Component
public class KeyMapper implements RowMapper<Key> {

    @Override
    public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
        Key k = new Key();
        k.setValue(rs.getString("value"));
        k.setRemainedUse(rs.getInt("usages_left"));
        k.setEmail(rs.getString("email"));

        return k;
    }

}
