package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.model.FifaTeam;
import com.mckinleyit.wcbml.parser.FifaTeamNameParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Step4:
 * Extract Team names and short codes from Fifa Rankings Page and save them to file.
 */
@Slf4j
public class Step4 implements Step {

    @Parameter(names = {"--input", "-i"}, description = "Path to Fifa html file", required = true)
    private String inputFile;

    @Parameter(names = {"--output", "-o"}, description = "Path to write team data", required = true)
    private String outputFile;

    private FifaTeamNameParser parser = new FifaTeamNameParser();


    @Override
    public void execute() throws StepExecutionException {
        try {
            String html = FileUtils.readFileToString(new File(inputFile), "UTF-8");
            List<FifaTeam> teams = parser.getTeamNames(html);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(teams);
            FileUtils.writeStringToFile(new File(outputFile), json, "UTF-8");
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save run step.", e);
        }
    }
}
