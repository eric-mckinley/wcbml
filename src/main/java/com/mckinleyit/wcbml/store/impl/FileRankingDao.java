package com.mckinleyit.wcbml.store.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mckinleyit.wcbml.model.FifaTeam;
import com.mckinleyit.wcbml.model.RankingData;
import com.mckinleyit.wcbml.model.TeamData;
import com.mckinleyit.wcbml.store.RankingDao;
import com.mckinleyit.wcbml.store.TeamDao;
import com.mckinleyit.wcbml.store.TeamNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileRankingDao implements RankingDao {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String rankingDataFolder;

    private final TeamDao teamDao;
    private final Map<String, List<RankingData>> rankingMap;

    public FileRankingDao(String rankingDataFolder, TeamDao teamDao) {
        this.rankingDataFolder = rankingDataFolder;
        this.teamDao = teamDao;
        this.rankingMap = new HashMap<>();
    }

    @Override
    public int getRanking(String teamName, LocalDate date)  {
        FifaTeam team = null;
        try {
            team = teamDao.getTeamByName(teamName);
        } catch (TeamNotFoundException e) {
            log.error("No ranking info available for team=" +teamName);
            return 300;
        }
        List<RankingData> rankingData = getRankingData(team);

        int backupTotalRanking = 0;

        for (int i = 1; i < rankingData.size(); i++) {
            RankingData ranking = rankingData.get(i - 1);
            RankingData nextRanking = rankingData.get(i);
            if (date.isAfter(ranking.getRankingDate().toLocalDate()) && date.isBefore(nextRanking.getRankingDate().toLocalDate())) {
                return ranking.getRanking();
            }
            backupTotalRanking += ranking.getRanking();
        }

        return backupTotalRanking / rankingData.size();
    }


    private List<RankingData> getRankingData(FifaTeam teamData) {
        if (rankingMap.containsKey(teamData.getShortCode())) {
            return rankingMap.get(teamData.getShortCode());
        } else {
            List<RankingData> rankingData = loadRankingData(teamData);
            rankingMap.put(teamData.getShortCode(), rankingData);
            return rankingData;
        }
    }

    private List<RankingData> loadRankingData(FifaTeam teamData){
        try {
            File rankingFile = new File(rankingDataFolder + "/ranking_" + teamData.getShortCode() + ".json");
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(rankingFile, typeFactory.constructCollectionType(List.class, RankingData.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load rankings for " + teamData.getShortCode(), e);
        }
    }
}
