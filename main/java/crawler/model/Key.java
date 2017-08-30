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
package crawler.model;

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
