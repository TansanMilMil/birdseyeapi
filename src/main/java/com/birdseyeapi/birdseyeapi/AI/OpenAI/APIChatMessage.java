package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true) // OpenAI APIのレスポンスは頻繁に変更されそうなので、不明なプロパティは無視する
public class APIChatMessage {
    private String role;
    private String content;
}
