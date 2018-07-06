package com.mckinleyit.wcbml.steps;

import com.beust.jcommander.converters.IParameterSplitter;

import java.util.Arrays;
import java.util.List;

public class CommaListSplitter  implements IParameterSplitter {
    public List<String> split(String value) {
        return Arrays.asList(value.split(","));
    }
}
