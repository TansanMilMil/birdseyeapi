package com.birdseyeapi.birdseyeapi.AI.Summarize;

public enum OpenAIMessageRole {
    System("system"),
    User("user");

    private final String strName;

    private OpenAIMessageRole(String strName) {
        this.strName = strName;
    }

    public String GetStrName() {
        return strName;
    }
}
