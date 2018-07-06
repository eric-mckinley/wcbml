package com.mckinleyit.wcbml.parser.sites;

import com.mckinleyit.wcbml.model.MatchScore;
import com.mckinleyit.wcbml.parser.ScoreParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ElevenVsElevenScoreParser implements ScoreParser {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final String PAGE_WRAP = "<html><body><table>%s</table></body></html>";

    @Override
    public List<MatchScore> parse(String text) throws Exception {
        text = String.format(PAGE_WRAP, text);
        List<MatchScore> matches = new ArrayList<>();

        Document document = Jsoup.parse(text);
        Elements elements = document.getElementsByTag("tbody");
        Element element = elements.first();
        Elements rows = element.getElementsByTag("tr");
        for (Element row : rows) {
            Elements cols = row.getElementsByTag("td");
            String dateText = cols.get(0).text();
            LocalDate date = parseDate(dateText);
            String[] teams = cols.get(1).text().split(" v ");
            String teamA = teams[0].replace(" ", "_").toUpperCase();
            String teamB = teams[1].replace(" ", "_").toUpperCase();

            String[] scores = cols.get(3).text().split("-");
            if(scores.length ==2) {
                Integer goalsA = Integer.valueOf(firstNumber(scores[0]));
                Integer goalsB = Integer.valueOf(firstNumber(scores[1]));
                String matchType = cols.get(4).text().replace(" ", "_").toUpperCase();

                MatchScore matchScore = new MatchScore();
                matchScore.setGoalA(goalsA);
                matchScore.setGoalB(goalsB);
                matchScore.setTeamA(teamA);
                matchScore.setTeamB(teamB);
                matchScore.setKickoff(date);
                matchScore.setRankingA(1);
                matchScore.setRankingB(2);
                matchScore.setMatchType(matchType);
                matchScore.setId(String.format("%s_%s_%s", dateText.replace(" ", "_"), teamA, teamB));

                matches.add(matchScore);
            }
        }
        return matches;
    }

    private String firstNumber(String s){
        StringBuilder builder = new StringBuilder();
        for(int i =0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c == ' ' && builder.length() > 0){
                return builder.toString();
            }
            else if (c != ' '){
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private LocalDate parseDate(String value) {
        return LocalDate.parse(value, formatter);
    }
}

