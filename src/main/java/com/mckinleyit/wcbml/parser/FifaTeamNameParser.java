package com.mckinleyit.wcbml.parser;

import com.mckinleyit.wcbml.model.FifaTeam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FifaTeamNameParser {

    public static final String TEAM_LINK_PREFIX = "/fifa-world-ranking/associations/association=";

    public List<FifaTeam> getTeamNames(String html) {
        List<FifaTeam>  teams = new ArrayList<>();
        html = String.format("<html><body>%s</body></html>", html);
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByTag("table").get(0);
        Elements rows  = table.getElementsByTag("tbody").get(0).getElementsByTag("tr");

        int shortLinkPrefixStart = TEAM_LINK_PREFIX.length();
        for(Element row : rows){
            Element teamNameCol = row.getElementsByClass("tbl-teamname").get(0);
            Element linkElement = teamNameCol.getElementsByTag("a").get(0);
            String teamLink = linkElement.attr("href");

            String teamShortCode = teamLink.substring(shortLinkPrefixStart, shortLinkPrefixStart +3);
            String teamName = linkElement.text();
            teams.add(new FifaTeam(teamName, teamShortCode));
        }
        return teams;
    }
}
