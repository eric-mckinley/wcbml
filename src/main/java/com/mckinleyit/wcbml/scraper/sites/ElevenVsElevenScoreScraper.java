package com.mckinleyit.wcbml.scraper.sites;

import com.mckinleyit.wcbml.scraper.JSoupUtil;
import com.mckinleyit.wcbml.scraper.ScoreScraper;
import com.mckinleyit.wcbml.scraper.ScrapeCriteria;
import com.mckinleyit.wcbml.scraper.ScrapedData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ElevenVsElevenScoreScraper implements ScoreScraper {

    public static final String URL = "https://www.11v11.com/teams/%s/tab/matches/season/%d/";


    @Override
    public List<ScrapedData> scrapeScores(ScrapeCriteria criteria) {
        try {

            List<ScrapedData> scrapedDataList = new ArrayList<>();
            int startYear = criteria.getStart().getYear();
            int endYear = criteria.getEnd().getYear();
            String team = criteria.getTeam();


            for (int year = startYear; year <= endYear; year++) {
                String url = resolveURL(team, year);
                Document document = JSoupUtil.getDocument(url);
                scrapedDataList.add(new ScrapedData(team + "_" + year, getMatches(document)));
                delay();
            }
            return scrapedDataList;
        } catch (IOException e) {
            log.error("Failed to resolve team={}", criteria, e);
            return Collections.emptyList();
        }
    }

    private void delay(){
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            log.error("Interupted!", e);
        }
    }

    private String resolveURL(String team, int year) {
        return String.format(URL, team, year).toLowerCase();
    }

    private String getMatches(Document document) {
        Element element = document.getElementById("pageContent");
        return element.getElementsByTag("table").html();
    }
}
