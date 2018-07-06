package com.mckinleyit.wcbml.parser;

import com.mckinleyit.wcbml.model.MatchScore;

import java.util.List;

public interface ScoreParser {
    List<MatchScore> parse(String text) throws Exception;
}
