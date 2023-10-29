package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIChatRequest {
    public String model;
    public List<APIChatMessage> messages;
}
