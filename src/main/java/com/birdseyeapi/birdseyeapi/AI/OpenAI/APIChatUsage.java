package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.Data;

@Data
public class APIChatUsage {
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
}
