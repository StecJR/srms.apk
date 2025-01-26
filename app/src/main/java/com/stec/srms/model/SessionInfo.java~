package com.stec.srms.model;

public class SessionInfo {
    public int sessionId;
    public String desc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE session_info")
                .append(" (")
                .append("sessionId INTEGER PRIMARY KEY, ")
                .append("desc TEXT")
                .append(");");
        return query.toString();
    }

    public SessionInfo() {
    }

    public SessionInfo(int sessionId, String desc) {
        this.sessionId = sessionId;
        this.desc = desc;
    }
}
