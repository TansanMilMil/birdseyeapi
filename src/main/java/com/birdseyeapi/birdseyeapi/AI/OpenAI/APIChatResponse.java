package com.birdseyeapi.birdseyeapi.AI.OpenAI;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true) // OpenAI APIのレスポンスは頻繁に変更されそうなので、不明なプロパティは無視する
public class APIChatResponse {
    private String id;
    private String object;
    private int created;
    private String model;
    private List<APIChatChoice> choices;
    private APIChatUsage usage;
}
