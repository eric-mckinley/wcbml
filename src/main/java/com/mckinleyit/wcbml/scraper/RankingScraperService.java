package com.mckinleyit.wcbml.scraper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

@Slf4j
public class RankingScraperService {

    public static final String URL = "https://www.fifa.com/fifa-world-ranking/ranking-table/men/index.html";

    public void scrapeRanking(String outputFile) throws IOException {
        Document document = JSoupUtil.getDocument(URL);
        Element table = document.getElementsByTag("table").get(0);
        saveTeamNames(outputFile, table.outerHtml());
    }

    private void saveTeamNames(String file, String html) throws IOException {
        FileUtils.writeStringToFile(new File(file), html, "UTF-8");
    }
}
