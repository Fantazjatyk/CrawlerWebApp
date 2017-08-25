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
package pl.michalszymanski.web.crawler.services;

import crawler.crawlers.Crawler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Micha� Szyma�ski, kontakt: michal.szymanski.aajar@gmail.com
 */
@Service
public final class CrawlerRegistry {

    private static AtomicReference<CrawlerRegistry> instance;

    private CrawlerRegistry() {

    }

    public static CrawlerRegistry getInstance() {

        synchronized (CrawlerRegistry.class) {
            if (CrawlerRegistry.instance == null) {
                CrawlerRegistry.instance = new AtomicReference(new CrawlerRegistry());
            }
        }
        return instance.get();
    }

    private final ConcurrentHashMap<String, Record> ACTIVE_SESSIONS = new ConcurrentHashMap();

    public void register(Record record) {
        this.ACTIVE_SESSIONS.put(record.getUserId(), record);
    }

    public void unregister(String userId) {
        this.ACTIVE_SESSIONS.remove(userId);
    }

    public int getActiveSessionsCount() {
        return this.ACTIVE_SESSIONS.size();
    }

    public boolean isAlreadyRegistered(Long id) {
        boolean condition = this.ACTIVE_SESSIONS.containsKey(id);
        return condition;
    }

    public static class Record {

        private final String userId;
        private final Class<? extends Crawler> type;

        public String getUserId() {
            return userId;
        }

        public Class<? extends Crawler> getType() {
            return type;
        }

        public Record(String userId, Class<? extends Crawler> type) {
            this.userId = userId;
            this.type = type;
        }
    }
}
