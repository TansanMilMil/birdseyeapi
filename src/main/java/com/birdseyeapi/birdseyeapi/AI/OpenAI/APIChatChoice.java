package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.Data;

@Data
public class APIChatChoice {
    public int index;
    public APIChatMessage message;
    public String finish_reason;
}
