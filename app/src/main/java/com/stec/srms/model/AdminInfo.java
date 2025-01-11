package com.stec.srms.model;

public class AdminInfo {
    public String adminName;
    public String adminPw;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE admin_info")
                .append(" (")
                .append("adminName TEXT, ")
                .append("adminPw TEXT NOT NULL, ")

                .append("PRIMARY KEY (adminName)")
                .append(")");
        return query.toString();
    }
}
