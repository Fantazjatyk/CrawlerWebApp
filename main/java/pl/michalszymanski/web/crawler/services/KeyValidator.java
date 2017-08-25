/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.michalszymanski.web.crawler.dao.KeysHolder;
import pl.michalszymanski.web.crawler.model.Key;
import pl.michalszymanski.web.crawler.model.ValidKey;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Component
public class KeyValidator implements ConstraintValidator<ValidKey, String> {

    @Autowired
    KeysHolder kh;

    @Override
    public void initialize(ValidKey constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Key k = kh.getKeyWrapper(value);
        return k != null && k.getRemainedUse() > 0;
    }

}
