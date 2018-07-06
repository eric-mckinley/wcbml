package com.mckinleyit.wcbml;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mckinleyit.wcbml.steps.StepExecutionException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class StepRunner {

    @Parameter(names = {"--step", "-s"}, description = "Step to run, value 1 to 4", required = true)
    private Integer step;

    public static void main(String[] args) throws Exception {
        StepRunner stepRunner = new StepRunner();

        List<String> allArgs = new ArrayList<>();
        allArgs.addAll(Arrays.asList(args));

        List<String> startArgs = new ArrayList<>();
        int stepIndex = allArgs.indexOf("-s") >= 0 ? allArgs.indexOf("-s") : allArgs.indexOf("--step");

        startArgs.add(allArgs.remove(stepIndex));
        startArgs.add(allArgs.remove(stepIndex));

        JCommander jCommander = new JCommander(stepRunner);
        jCommander.parse(startArgs.toArray(new String[0]));

        stepRunner.start(allArgs.toArray(new String[0]));
    }

    private void start(String[] args) {
        try {
            Class stepClass = Class.forName("com.mckinleyit.wcbml.steps.Step" + this.step);
            Step step = (Step) stepClass.newInstance();

            JCommander jCommander = new JCommander(step);
            jCommander.parse(args);

            step.execute();
        } catch (ClassNotFoundException e) {
            log.error("No Step class found for step={}", step, e);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Cannot create  instance for step={}", step, e);
        } catch (StepExecutionException e) {
            log.error("Failed to run, step={}", step, e);
        }
    }
}
