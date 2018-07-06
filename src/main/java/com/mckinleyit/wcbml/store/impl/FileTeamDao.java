package com.mckinleyit.wcbml.store.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mckinleyit.wcbml.model.FifaTeam;
import com.mckinleyit.wcbml.model.TeamData;
import com.mckinleyit.wcbml.store.TeamDao;
import com.mckinleyit.wcbml.store.TeamNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileTeamDao implements TeamDao {

    private final List<FifaTeam> dataList;

    public FileTeamDao(String file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            this.dataList = objectMapper.readValue(new File(file), typeFactory.constructCollectionType(List.class, FifaTeam.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to initialise dao, file=" + file, e);
        }
    }

    @Override
    public FifaTeam getTeamByShortCode(String code) throws TeamNotFoundException {
        for (FifaTeam teamData : dataList) {
            if (teamData.getShortCode().equals(code)) {
                return teamData;
            }
        }
        throw new TeamNotFoundException("Team not found for shortCode=" + code);
    }

    @Override
    public FifaTeam getTeamByName(String name) throws TeamNotFoundException {
        String checkName=  name.replace("_", "-");
        for (FifaTeam teamData : dataList) {

            if (teamData.getScoreName().equalsIgnoreCase(checkName)) {
                return teamData;
            }
        }
        throw new TeamNotFoundException("Team not found for name=" + name);
    }
}
