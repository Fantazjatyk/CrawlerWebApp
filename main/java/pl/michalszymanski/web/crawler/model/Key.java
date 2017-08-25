/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.model;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Key {

    private int remainedUse;
    private String value;
    private String email;

    public Key(String value, int remainedUse) {
        this.remainedUse = remainedUse;
        this.value = value;
    }

    public Key(int remainedUse, String value, String email) {
        this.remainedUse = remainedUse;
        this.value = value;
        this.email = email;
    }

    public Key() {

    }

    public int getRemainedUse() {
        return remainedUse;
    }

    public String getValue() {
        return value;
    }

    public String getEmail() {
        return email;
    }

    public void setRemainedUse(int remainedUse) {
        this.remainedUse = remainedUse;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
