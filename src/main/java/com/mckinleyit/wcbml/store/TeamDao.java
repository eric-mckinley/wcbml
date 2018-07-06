package com.mckinleyit.wcbml.store;

import com.mckinleyit.wcbml.model.FifaTeam;

public interface TeamDao {

    FifaTeam getTeamByShortCode(String code) throws TeamNotFoundException;

    FifaTeam getTeamByName(String name) throws TeamNotFoundException;
}
