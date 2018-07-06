package com.mckinleyit.wcbml;

import com.mckinleyit.wcbml.steps.StepExecutionException;

public interface Step {

    void execute() throws StepExecutionException;
}
