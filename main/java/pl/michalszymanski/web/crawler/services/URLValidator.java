/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pl.michalszymanski.web.crawler.model.ValidURL;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class URLValidator implements ConstraintValidator<ValidURL, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        URL test;
        try {
            test = new URL(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ValidURL constraintAnnotation) {
    }

}
