package com.mckinleyit.wcbml.store;

import java.time.LocalDate;

public interface RankingDao {

     int getRanking(String teamName, LocalDate date);
}
