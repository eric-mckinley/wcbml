package com.mckinleyit.wcbml.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RankingData implements Comparable<RankingData> {

    @JsonProperty("deliverydate")
    @JsonDeserialize(using = EventTimeDeserializer.class)
    private LocalDateTime rankingDate;


    @JsonProperty("rankseq")
    private int ranking;

    public RankingData(LocalDateTime rankingDate, int ranking) {
        this.rankingDate = rankingDate;
        this.ranking = ranking;
    }

    public RankingData() {
    }

    @Override
    public int compareTo(RankingData o) {
        return this.rankingDate.compareTo(o.getRankingDate());
    }
}
