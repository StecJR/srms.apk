package com.stec.srms.model;

public class SessionInfo {
    public int sessionId;
    public String desc;

    public SessionInfo() {
    }

    public SessionInfo(int sessionId, String desc) {
        this.sessionId = sessionId;
        this.desc = desc;
    }

    public static String getQuery() {
        return "CREATE TABLE session_info" +
                " (" +
                "sessionId INTEGER PRIMARY KEY, " +
                "desc TEXT" +
                ");";
    }
}
