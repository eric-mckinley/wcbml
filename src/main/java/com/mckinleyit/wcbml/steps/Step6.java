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
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Go through all the Fifa Team names and if not finding score data for the that team then manually add score name for that team.
 */
@Slf4j
public class Step6 implements Step {


    @Parameter(names = {"--input", "-i"}, description = "Path to Fifa teams json file", required = true)
    private String inputFile;

    @Parameter(names = {"--teams", "-t"}, description = "Folder where team score data", required = true)
    private String teamsFolder;

    private FifaTeamNameParser parser = new FifaTeamNameParser();


    @Override
    public void execute() throws StepExecutionException {
        try {
            File teamFile = new File(inputFile);
            ObjectMapper objectMapper = new ObjectMapper();
            List<FifaTeam> teams = objectMapper.readValue(teamFile, new TypeReference<List<FifaTeam>>(){});
            for (FifaTeam fifaTeam : teams) {
                if(fifaTeam.getScoreName() == null) {
                    String name = fifaTeam.getName().toLowerCase().replaceAll(" ", "-");
                    File file = new File(teamsFolder + "/" + name + "_2017.html");
                    if (!file.exists()) {
                        throw new StepExecutionException("Team " + fifaTeam.getName() + " needs manually set scoreName");
                    } else {
                        fifaTeam.setScoreName(name);
                        objectMapper.writeValue(teamFile, teams);
                    }
                }
            }
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save run step.", e);
        }
    }
}
