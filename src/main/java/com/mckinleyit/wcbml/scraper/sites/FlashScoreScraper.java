package com.mckinleyit.wcbml.scraper.sites;

import com.mckinleyit.wcbml.scraper.ScoreScraper;
import com.mckinleyit.wcbml.scraper.ScrapeCriteria;
import com.mckinleyit.wcbml.scraper.ScrapedData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Test of flashscore shows they prevent scraping by not using standard html scr output
 */
@Slf4j
@Deprecated
public class FlashScoreScraper implements ScoreScraper {

    public static final String MATCHES_BODY = "fsbody";

    @Override
    public List<ScrapedData> scrapeScores(ScrapeCriteria criteria) {
        return null;
    }

    public Optional<String> scrapeScores(String team) {
        try {
            String url = resolveURL(team);
            Document document = getDocument(url);
            return Optional.of(getMatches(document));
        } catch (IOException e) {
            log.error("Failed to resolve team={}", team, e);
            return Optional.empty();
        }
    }

    private String resolveURL(String team) {
        return "https://www.flashscore.com/team/austria/naHiWdnt/results/";
    }

    private String getMatches(Document document) {
        Element element = document.getElementById(MATCHES_BODY);
        return element.html();
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.parse(new URL(url), 2500);
    }
}
