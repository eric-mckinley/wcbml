package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.model.FifaTeam;
import com.mckinleyit.wcbml.parser.FifaTeamNameParser;
import com.mckinleyit.wcbml.scraper.RankingScraperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Step4:
 * Download list of Fifa team names to help get the historic ranking data
 */
@Slf4j
public class Step3 implements Step {

    @Parameter(names = {"--outputFile", "-o"}, description = "Path to write ranking data", required = true)
    private String outputFile;

    private FifaTeamNameParser parser = new FifaTeamNameParser();


    @Override
    public void execute() throws StepExecutionException {
        try {
            RankingScraperService rankingScraperService = new RankingScraperService();
            rankingScraperService.scrapeRanking(outputFile);
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save run step.", e);
        }
    }
}
