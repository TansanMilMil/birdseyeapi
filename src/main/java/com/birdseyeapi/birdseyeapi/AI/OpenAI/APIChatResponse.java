package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatResponse {
    private String id;
    private String object;
    private int created;
    private String model;
    private List<APIChatChoice> choices;
    private APIChatUsage usage;
}
