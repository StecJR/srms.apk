package com.stec.srms.model;

public class GuardianInfo {
    public int guardianId;
    public String name;
    public String relation;
    public String contact;
    public String email;
    public int studentId;
    public int deptId;
    public String password;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE guardians")
                .append(" (")
                .append("guardianId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("name TEXT, ")
                .append("relation TEXT, ")
                .append("contact TEXT, ")
                .append("email TEXT UNIQUE, ")
                .append("studentId INTEGER, ")
                .append("deptId INTEGER, ")
                .append("password TEXT, ")

                .append("FOREIGN KEY (deptId) REFERENCES dept_info(deptId)")
                .append(");");
        return query.toString();
    }

    public GuardianInfo() {
    }

    public GuardianInfo(int guardianId, String name, String relation, String contact, String email, int studentId, int deptId, String password) {
        this.guardianId = guardianId;
        this.name = name;
        this.relation = relation;
        this.contact = contact;
        this.email = email;
        this.studentId = studentId;
        this.deptId = deptId;
        this.password = password;
    }
}
