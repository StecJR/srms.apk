package com.stec.srms.model;

public class SemesterInfo {
    public int semesterId;
    public String shortDesc;
    public String longDesc;

    public SemesterInfo() {}
    public SemesterInfo(int semesterId, String shortDesc, String longDesc) {
        this.semesterId = semesterId;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public static String getQuery() {
        return "CREATE TABLE semester_info" +
                " (" +
                "semesterId INTEGER PRIMARY KEY, " +
                "shortDesc TEXT, " +
                "longDesc TEXT" +
                ");";
    }
}
