package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mckinleyit.wcbml.Step;
import com.mckinleyit.wcbml.model.MatchScore;
import com.mckinleyit.wcbml.store.TeamDao;
import com.mckinleyit.wcbml.store.TeamNotFoundException;
import com.mckinleyit.wcbml.store.impl.FileRankingDao;
import com.mckinleyit.wcbml.store.impl.FileTeamDao;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Iterate all the matches and assign the rankings to the home and away team
 */
public class Step7 implements Step {


    @Parameter(names = {"--matches", "-m"}, description = "Path to all matches", required = true)
    private String matchesFolder;

    @Parameter(names = {"--teams", "-t"}, description = "File with team names", required = true)
    private String teamsFile;

    @Parameter(names = {"--rankings", "-r"}, description = "Path to all rankings", required = true)
    private String rankingsFolder;

    @Parameter(names = {"--outputFolder", "-o"}, description = "Path to save matches", required = true)
    private String outputFolder;


    private FileRankingDao rankingDao;

    private ObjectMapper objectMapper;

    private LocalDate stepRunDate = LocalDate.now();
    public Step7() {
    }

    @Override
    public void execute() throws StepExecutionException {
        TeamDao teamDao = new FileTeamDao(teamsFile);
        this.rankingDao = new FileRankingDao(rankingsFolder, teamDao);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        List<MatchScore> matchScores = loadAllScores();
            for (MatchScore score : matchScores) {
                score.setRankingA(this.rankingDao.getRanking(score.getTeamA(), score.getKickoff()));
                score.setRankingB(this.rankingDao.getRanking(score.getTeamB(), score.getKickoff()));
                score.setMonthsSinceKickoff(getMonthsSinceKickoff(score.getKickoff()));
                saveMatch(score);
            }
    }

    private int getMonthsSinceKickoff(LocalDate date){
     return (int)ChronoUnit.MONTHS.between(date, stepRunDate);
    }

    private List<MatchScore> loadAllScores() throws StepExecutionException {

        List<MatchScore> scores = new ArrayList<>();
        File[] files = new File(matchesFolder).listFiles();
        for (File file : files) {
            try {
                MatchScore score = objectMapper.readValue(file, MatchScore.class);
                scores.add(score);
            } catch (IOException e) {
                throw new StepExecutionException("Failed load matchscore, file=" + file.getName(), e);
            }
        }
        return scores;
    }

    private void saveMatch(MatchScore score) throws StepExecutionException {
        try {
            String json = objectMapper.writeValueAsString(score);
            FileUtils.writeStringToFile(
                    new File(String.format("%s/%s.json", outputFolder, score.getId())),
                    json,
                    "UTF-8"
            );
        } catch (IOException e) {
            throw new StepExecutionException("Failed to save match id=" + score.getId(), e);
        }
    }
}
