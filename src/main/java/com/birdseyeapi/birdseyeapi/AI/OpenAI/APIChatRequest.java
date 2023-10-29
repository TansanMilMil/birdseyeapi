package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatRequest {
    private String model;
    private List<APIChatMessage> messages;
}
