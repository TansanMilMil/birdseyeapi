package com.birdseyeapi.birdseyeapi.AI.Summarize;

import java.io.IOException;

public interface AISummarizer {
    public String summarize(String text) throws IOException, InterruptedException;
}
