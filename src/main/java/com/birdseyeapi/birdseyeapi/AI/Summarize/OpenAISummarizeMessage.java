package com.birdseyeapi.birdseyeapi.AI.Summarize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAISummarizeMessage {
    public String role;
    public String content;
}
