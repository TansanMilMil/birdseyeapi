package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatMessage {
    public String role;
    public String content;
}
