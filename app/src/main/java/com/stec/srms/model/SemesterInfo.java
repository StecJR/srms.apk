package com.stec.srms.model;

public class SemesterInfo {
    public int semesterId;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE semester_info")
                .append(" (")
                .append("semesterId INTEGER PRIMARY KEY, ")
                .append("shortDesc TEXT, ")
                .append("longDesc TEXT")
                .append(");");
        return query.toString();
    }

    public SemesterInfo() {
    }

    public SemesterInfo(int semesterId, String shortDesc, String longDesc) {
        this.semesterId = semesterId;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
