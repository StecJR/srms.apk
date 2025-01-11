package com.stec.srms.model;

public class DeptInfo {
    public Integer deptId;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE dept_info")
                .append(" (")
                .append("deptId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("shortDesc TEXT NOT NULL, ")
                .append("longDesc TEXT NOT NULL")
                .append(")");
        return query.toString();
    }
}
