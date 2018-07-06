package com.mckinleyit.wcbml.scraper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScrapedData {

    private String identifier;
    private String html;

    public String getHtml() {
        return html;
    }

    public String getIdentifier() {
        return identifier;
    }
}
