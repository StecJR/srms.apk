package com.stec.srms.model;

public class SemesterInfo {
    public Integer semesterId;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE semester_info")
                .append(" (")
                .append("semesterId INTEGER, ")
                .append("shortDesc TEXT NOT NULL, ")
                .append("longDesc TEXT NOT NULL, ")

                .append("PRIMARY KEY (semesterId)")
                .append(")");
        return query.toString();
    }
}
