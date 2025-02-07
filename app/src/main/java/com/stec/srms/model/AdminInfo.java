package com.stec.srms.model;

public class AdminInfo {
    public String adminName;
    public String adminPw;

    public AdminInfo() {
    }

    public AdminInfo(String adminName, String adminPw) {
        this.adminName = adminName;
        this.adminPw = adminPw;
    }

    public static String getQuery() {
        return "CREATE TABLE admin_info" +
                " (" +
                "adminName TEXT PRIMARY KEY, " +
                "adminPw TEXT" +
                ");";
    }
}
