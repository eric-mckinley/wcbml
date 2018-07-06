# WORLD CUP BETTING MACHINE LEARNING

Crude betting predictor for worldcup, may not get it finished, started too late.
Going to build a model, based on teams, rankings and scores from last 12 years and see what estimates it creates.
Will use the WEKA libraries for this.



## Step 1:
- [x] Scrapes the html from 11v11 website into a folder

Run with:
 
 `java -jar built-wbcml.jar --step 1 --outputFolder data/step1/out/ --startYear 2007 --endYear 2018 --countries lithuania,england,scotland....`

## Step 2:
- [x] Loads the scraped html files
- [x] Parses and extracts the match scores from each file.
- [x] Saves each match as a json file

Run with:
 
 `java -jar built-wbcml.jar --step 2 --inputFolder data/step1/out/ --outputFolder data/step2/out`

## Step 3:
- [x] Download list of Fifa team names to help get the historic ranking data

Run with:
 
 `java -jar built-wbcml.jar --step 3 --outputFile data/step3/out/fifa_all_teams.html`

## Step 4:
- [x] Extract Team names and short codes from Fifa Rankings Page and save them to file.

Run with:
 
 `java -jar built-wbcml.jar --step 4 --input data/step3/out/fifa_all_teams.html --output data/step4/out/fullteams.json`

## Step 5:
- [x] Load all the FIFA team names.
- [x] Use each team short code to download their history ranking data from fifa
- [x] Save each teams ranking data as a json file

Run with:
 
 `java -jar built-wbcml.jar --step 5  --input data/step4/out/fullteams.json --output data/step5/out`

## Step 6:
- [x] Go through all the Fifa Team names and if not finding score data for the that team then manually add score name for that team.

Run with:
 
 `java -jar built-wbcml.jar --step 6  --input data/step4/out/fullteams.json --teams data/step1/out`

## Step 7:
- [x] Go through all the matches and using the scraped ranking data update the match with the ranking data of the teams at that time

Run with:
 
 `java -jar built-wbcml.jar --step 7  -m data/step2/out -t data/step4/out/fullteams.json -r data/step5/out -o data/step7/out`

## Step 8:
- [ ] Create World cup group stages matches without score

## Step 9:
- [ ] Create Weka Model from matches using different classifications.

## Step 10:
- [ ] From the model instances predict group stage matches.
