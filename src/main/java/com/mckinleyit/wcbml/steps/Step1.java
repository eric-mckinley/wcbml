package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.scraper.ScrapeCriteria;
import com.mckinleyit.wcbml.scraper.ScoreScraperService;
import com.mckinleyit.wcbml.scraper.sites.ElevenVsElevenScoreScraper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;


/**
 * Step1:
 * Scrapes the html from 11v11 website into a folder
 */
@Slf4j
public class Step1 implements Step {

    @Parameter(names = {"--outputFolder", "-o"}, description = "Path to write team data", required = true)
    private String outputFolder;

    @Parameter(names = {"--countries", "-c"}, splitter = CommaListSplitter.class, required = true)
    private List<String> countries;

    @Parameter(names = {"--startYear", "-s"}, description = "Start year to scrape from")
    private Integer startYear = 2007;

    @Parameter(names = {"--endYear", "-e"}, description = "End year to scrape to")
    private Integer endYear = 2018;


    @Override
    public void execute() throws StepExecutionException {

        ScoreScraperService service = new ScoreScraperService(outputFolder, new ElevenVsElevenScoreScraper());

        for (String country : countries) {
            ScrapeCriteria criteria = buildCriteria(country);
            service.scrapeTeam(criteria);
        }
    }

    private ScrapeCriteria buildCriteria(String team) {
        ScrapeCriteria criteria = new ScrapeCriteria();
        criteria.setStart(LocalDate.of(startYear, 1, 1));
        criteria.setEnd(LocalDate.of(endYear, 12, 31 ));
        criteria.setTeam(team);
        return criteria;
    }
}
