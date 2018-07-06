package com.mckinleyit.wcbml.scraper;

import java.util.List;

public interface ScoreScraper {

    List<ScrapedData> scrapeScores(ScrapeCriteria criteria);
}
