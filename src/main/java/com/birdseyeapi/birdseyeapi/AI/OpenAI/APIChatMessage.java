package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIChatMessage {
    private String role;
    private String content;
}
