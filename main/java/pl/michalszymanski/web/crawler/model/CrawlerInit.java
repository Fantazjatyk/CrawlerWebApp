/*
 * The MIT License
 *
 * Copyright 2017 Micha� Szyma�ski, kontakt: michal.szymanski.aajar@gmail.com.
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
package pl.michalszymanski.web.crawler.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.michalszymanski.web.crawler.services.StringArrayTrimmedDeserializer;


/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class CrawlerInit {

    @JsonDeserialize(using=StringArrayTrimmedDeserializer.class)
    @JsonSerialize(using = StringArraySerializer.class)
    private String[] sentences;
    @NotNull
    @Min(value = 0, message = "Time limit cannot be lower than 0")
    @Max(360)
    private int timeLimit;
    String type;
    @NotNull
    @Size(min = 5, max = 500, message = "URL size must be between {min} and {max} characters")
    @ValidURL
    private String url;

    @Max(360)
    private String email;

    @ValidKey
    private String key;

    private boolean ignoreCase = false;

    public boolean getIgnoreCase() {
        return ignoreCase;
    }

    public String[] getSentences() {
        return sentences;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public void setSentences(String[] sentences) {
        this.sentences = sentences;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

}
