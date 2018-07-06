package com.mckinleyit.wcbml.steps;

public class StepExecutionException extends Exception {

    public StepExecutionException(String message) {
        super(message);
    }

    public StepExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
