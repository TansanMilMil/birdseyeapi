package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import java.util.List;

import lombok.Data;

@Data
public class APIChatResponse {
    public String id;
    public String object;
    public int created;
    public String model;
    public List<APIChatChoice> choices;
    public APIChatUsage usage;
}
