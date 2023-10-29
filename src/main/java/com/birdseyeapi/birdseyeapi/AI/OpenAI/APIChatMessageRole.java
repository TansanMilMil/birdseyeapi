package com.birdseyeapi.birdseyeapi.AI.OpenAI;

public enum APIChatMessageRole {
    System("system"),
    User("user");

    private final String strName;

    private APIChatMessageRole(String strName) {
        this.strName = strName;
    }

    public String GetStrName() {
        return strName;
    }
}
