package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.model.MatchScore;
import com.mckinleyit.wcbml.parser.sites.ElevenVsElevenScoreParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Step2:
 * Loads the scraped html files
 * Parses and extracts the match scores from each file.
 * Saves each match as a json file
 */
@Slf4j
public class Step2 implements Step {

    private int c;

    @Parameter(names = {"--inputFolder", "-i"}, description = "Path to read scraped html from", required = true)
    private String inputFolder;

    @Parameter(names = {"--outputFolder", "-o"}, description = "Path to write parsed teams to", required = true)
    private String outputFolder;

    private final ObjectMapper objectMapper;

    public Step2() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void execute() throws StepExecutionException {
        File[] files = new File(inputFolder).listFiles();
        if(files == null){
            throw new StepExecutionException("No html files in folder: " + inputFolder);
        }
        ElevenVsElevenScoreParser parser = new ElevenVsElevenScoreParser();
        List<MatchScore> matchScores = new ArrayList<>();
        for (File file : files) {
            try {
                String html = loadMatchHtml(file);
                List<MatchScore> parsedScores = parser.parse(html);
                matchScores.addAll(parsedScores);
            } catch (Exception e) {
                e.printStackTrace();
                throw new StepExecutionException("Problem with file: " + file.getName() +", " + e.getMessage());
            }
        }
        for(MatchScore score: matchScores){
            saveMatch(score);
        }
    }

    private String loadMatchHtml(File file) throws StepExecutionException {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new StepExecutionException("Failed to load matches file=" + file.getName(), e);
        }
    }

    private void saveMatch(MatchScore score) throws StepExecutionException {
        try {
            String json = objectMapper.writeValueAsString(score);
            FileUtils.writeStringToFile(
                    new File(String.format("%s/%s.json", outputFolder, score.getId())),
                    json,
                    "UTF-8"
            );
            log.info((c++) + " saved data to " + score.getId());
        } catch (IOException e) {
           throw new StepExecutionException("Failed to save match id=" + score.getId(), e);
        }
    }
}
