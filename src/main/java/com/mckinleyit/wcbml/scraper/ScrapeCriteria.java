package com.mckinleyit.wcbml.scraper;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class ScrapeCriteria {

    private String team;
    private LocalDate start;
    private LocalDate end;

}
