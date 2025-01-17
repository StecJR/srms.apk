package com.stec.srms.model;

public class DeptInfo {
    public int deptId;
    public String shortDesc;
    public String longDesc;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE dept_info")
                .append(" (")
                .append("deptId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("shortDesc TEXT, ")
                .append("longDesc TEXT")
                .append(");");
        return query.toString();
    }

    public DeptInfo() {
    }

    public DeptInfo(int deptId, String shortDesc, String longDesc) {
        this.deptId = deptId;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }
}
