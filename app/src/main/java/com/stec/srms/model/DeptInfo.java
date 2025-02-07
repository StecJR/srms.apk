package com.stec.srms.model;

public class DeptInfo {
    public int deptId;
    public String shortDesc;
    public String longDesc;

    public DeptInfo() {
    }

    public DeptInfo(int deptId, String shortDesc, String longDesc) {
        this.deptId = deptId;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public static String getQuery() {
        return "CREATE TABLE dept_info" +
                " (" +
                "deptId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shortDesc TEXT, " +
                "longDesc TEXT" +
                ");";
    }
}
