package com.mckinleyit.wcbml.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
public class MatchScore {

    private String id;
    private String teamA;
    private String teamB;
    private Integer goalA;
    private Integer goalB;
    private Integer rankingA;
    private Integer rankingB;
    private String matchType;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate kickoff;

    private Integer monthsSinceKickoff;
}
