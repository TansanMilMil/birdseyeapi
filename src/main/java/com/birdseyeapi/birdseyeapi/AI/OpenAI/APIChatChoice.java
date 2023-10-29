package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatChoice {
    private int index;
    private APIChatMessage message;
    private String finish_reason;
}
