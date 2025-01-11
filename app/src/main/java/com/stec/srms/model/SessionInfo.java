package com.stec.srms.model;

public class SessionInfo {
    public Integer sessionId;
    public String desc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE session_info")
                .append(" (")
                .append("sessionId INTEGER, ")
                .append("desc TEXT NOT NULL, ")

                .append("PRIMARY KEY (sessionId)")
                .append(")");
        return query.toString();
    }
}
