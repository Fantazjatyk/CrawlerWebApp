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

import crawler.data.ImageSource;
import crawler.data.Sentence;
import java.util.Collection;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class CrawlerResult {

    private String target;
    private double time;
    private Collection<Sentence> sentences;
    private String[] sentencesInit;
    private Collection<ImageSource> images;

    public String getTarget() {
        return target;
    }

    public double getTime() {
        return time;
    }

    public void setSentencesInit(String[] sentencesInit) {
        this.sentencesInit = sentencesInit;
    }

    public String[] getSentencesInit() {
        return sentencesInit;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Collection<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(Collection<Sentence> sentences) {
        this.sentences = sentences;
    }

    public Collection<ImageSource> getImages() {
        return images;
    }

    public void setImages(Collection<ImageSource> images) {
        this.images = images;
    }



}
