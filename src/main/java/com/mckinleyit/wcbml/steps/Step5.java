package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.model.FifaTeam;
import com.mckinleyit.wcbml.parser.FifaTeamNameParser;
import com.mckinleyit.wcbml.scraper.JSoupUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Load all the FIFA team names.
 * Use each team short code to download their history ranking data from fifa
 * Save each teams ranking data as a json file
 */
@Slf4j
public class Step5 implements Step {

    public static final String RANKING_URL = "http://www.fifa.com/common/fifa-world-ranking/ma=%s/_history.js";

    @Parameter(names = {"--input", "-i"}, description = "Path to Fifa teams json file", required = true)
    private String inputFile;

    @Parameter(names = {"--output", "-o"}, description = "Folder to write team ranking data", required = true)
    private String outputFile;

    private FifaTeamNameParser parser = new FifaTeamNameParser();


    @Override
    public void execute() throws StepExecutionException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<FifaTeam> teams = objectMapper.readValue(new File(inputFile), new TypeReference<List<FifaTeam>>(){});
            for (FifaTeam fifaTeam : teams) {
                String data = loadTeamRankingData(fifaTeam);
                saveTeamRankingData(data, fifaTeam);
            }
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save run step.", e);
        }
    }

    private String loadTeamRankingData(FifaTeam team) throws StepExecutionException {
        try {
            String dataUrl = String.format(RANKING_URL, team.getShortCode());
            Document rankingData = JSoupUtil.getDocument(dataUrl);
            return rankingData.text();
        } catch (IOException e) {
            throw new StepExecutionException("Failed to load team ranking data, team=" + team.getName(), e);

        }
    }

    private void saveTeamRankingData(String data, FifaTeam team) throws StepExecutionException {
        try {
            FileUtils.writeStringToFile(new File(outputFile + "/ranking_" + team.getShortCode() + ".json"), data, "UTF-8");
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save team ranking data, team=" + team.getName(), e);
        }

    }
}
