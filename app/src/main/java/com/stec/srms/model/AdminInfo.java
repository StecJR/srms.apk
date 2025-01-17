package com.stec.srms.model;

public class AdminInfo {
    public String adminName;
    public String adminPw;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE admin_info")
                .append(" (")
                .append("adminName TEXT PRIMARY KEY, ")
                .append("adminPw TEXT")
                .append(");");
        return query.toString();
    }

    public AdminInfo() {
    }

    public AdminInfo(String adminName, String adminPw) {
        this.adminName = adminName;
        this.adminPw = adminPw;
    }
}
