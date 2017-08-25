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

import pl.michalszymanski.web.crawler.model.CrawlerResult;
import pl.michalszymanski.web.crawler.model.CrawlerInit;
import crawler.configuration.CrawlerConfiguration;
import crawler.configuration.CrawlerParams;
import crawler.crawlers.Crawler;
import crawler.crawlers.continous.FlexibleContinousCrawler;
import crawler.crawlers.oneshot.FlexibleOneShotCrawler;
import crawler.exception.InitialConfigurationException;
import crawler.scrapping.collectors.Collector;
import crawler.scrapping.collectors.DataHolderCollector;
import crawler.scrapping.collectors.ImagesCollector;
import crawler.scrapping.collectors.SentencesCollector;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import michal.szymanski.util.Arrays;
import michal.szymanski.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Micha� Szyma�ski, kontakt: michal.szymanski.aajar@gmail.com
 */
@Service
public class CrawlersManager {

    private CrawlerRegistry registry = CrawlerRegistry.getInstance();

    public CrawlerResult runCrawler(CrawlerInit init, String userId) {

        final Crawler crawler = getCrawler(init);
        registerInRegistry(userId, crawler);
        try {
            synchronized (crawler) {
                crawler.start();
            }
        } catch (InitialConfigurationException e) {
            throw e;
        } finally {
            unregisterFromRegistry(userId);
        }
        return prepareResponse(crawler);
    }

    private boolean isOneShot(CrawlerInit init) {
        return !(init.getTimeLimit() > 0);
    }

    private Crawler getCrawler(CrawlerInit init) {
        Crawler crawler = isOneShot(init) ? new FlexibleOneShotCrawler() : new FlexibleContinousCrawler();
        SentencesCollector s = new SentencesCollector();
        s.setIgnoreCase(init.getIgnoreCase());
        s.setTarget(init.getSentences());
        crawler.configure()
                .initUrl(init.getUrl())
                .timeLimit(init.getTimeLimit())
                .addCollector(s)
                .addCollector(new ImagesCollector());
        return crawler;
    }

    private CrawlerResult prepareResponse(Crawler crawler) {
        Map<Class, DataHolderCollector> collectors = crawler.getConf().getCollectors();
        Set<Class> keys = collectors.keySet();
        CrawlerResult response = new CrawlerResult();

        if (keys.contains(ImagesCollector.class)) {
            response.setImages(collectors.get(ImagesCollector.class).getResultsWithoutDuplicates());
        }

        if (keys.contains(SentencesCollector.class)) {
            SentencesCollector sc = (SentencesCollector) collectors.get(SentencesCollector.class);
            response.setSentences(sc.getResultsWithoutDuplicates());
            String[] initSentences = (String[]) sc.getFilteringSentences().toArray();
            response.setSentencesInit(initSentences);
        }

        response.setTime(crawler.getElapsedTime());

        response.setTarget(crawler.getConf().getInitURL());
        return response;
    }

    private void registerInRegistry(String id, Crawler crawler) {
        CrawlerRegistry.Record record = new CrawlerRegistry.Record(id, crawler.getClass());
        registry.register(record);
    }

    private void unregisterFromRegistry(String id) {
        registry.unregister(id);
    }

}
