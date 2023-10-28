package com.birdseyeapi.birdseyeapi.AI.Summarize;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OpenAISummarizeResult {
    public String id;
    public String object;
    public int created;
    public String model;
    public List<OpenAISummarizeResultChoice> choices;
    public OpenAISummarizeResultUsage usage;
}

@Data
class OpenAISummarizeResultChoice {
    public int index;
    public OpenAISummarizeMessage message;
    public String finish_reason;
}

@Data
@NoArgsConstructor
class OpenAISummarizeResultUsage {
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
}