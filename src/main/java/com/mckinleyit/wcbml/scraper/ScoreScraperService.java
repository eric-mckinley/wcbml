package com.mckinleyit.wcbml.scraper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ScoreScraperService {

    private final String dataHtmlDirectory;
    private final ScoreScraper scoreScraper;

    public ScoreScraperService(String dataHtmlDirectory, ScoreScraper scoreScraper) {
        this.dataHtmlDirectory = dataHtmlDirectory;
        this.scoreScraper = scoreScraper;
    }

    public void scrapeTeam(ScrapeCriteria criteria) {
        log.info("Scraping team={}", criteria);
        List<ScrapedData> dataList = scoreScraper.scrapeScores(criteria);
        for (ScrapedData scrapedData : dataList) {
            saveTeamScores(scrapedData.getIdentifier(), scrapedData.getHtml());
        }
    }

    private void saveTeamScores(String teamName, String html) {
        try {
            String fname = String.format("%s/%s.html", dataHtmlDirectory, teamName.toLowerCase());
            FileUtils.writeStringToFile(new File(fname), html, "UTF-8");
        } catch (IOException e) {
            log.error("Failed to save team html, team=", teamName, e);
        }
    }
}
