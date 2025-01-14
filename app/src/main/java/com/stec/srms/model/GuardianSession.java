package com.stec.srms.model;

public class GuardianSession {
    int guardianId;
    int deptId;
    int studentId;

    public GuardianSession(int guardianId, int deptId, int studentId) {
        this.guardianId = guardianId;
        this.deptId = deptId;
        this.studentId = studentId;
    }
}
