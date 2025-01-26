package com.stec.srms.model;

public class PendingFaculties {
    public int userId;
    public String name;
    public String gender;
    public int deptId;
    public String contact;
    public String email;
    public String address;
    public String password;

    public PendingFaculties() {}
    public PendingFaculties(int userId, String name, String gender, int deptId, String contact, String email, String address, String password) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.deptId = deptId;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public static String getQuery() {
        return "CREATE TABLE pending_faculties" +
                " (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "gender TEXT, " +
                "deptId INTEGER, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "address TEXT, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId)" +
                ");";
    }
}
