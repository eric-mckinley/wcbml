package com.mckinleyit.wcbml.model;

import lombok.Data;

@Data
public class FifaTeam {

    private String name;
    private String shortCode;
    private String scoreName;

    public FifaTeam() {
    }

    public FifaTeam(String name, String shortCode) {
        this.name = name;
        this.shortCode = shortCode;
    }
}
