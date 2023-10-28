package com.birdseyeapi.birdseyeapi.AI.Summarize;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenAISummarizeRequest {
    public String model;
    public List<OpenAISummarizeMessage> messages;
}
