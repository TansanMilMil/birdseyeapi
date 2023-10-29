package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatUsage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
}
