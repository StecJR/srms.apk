package com.stec.srms.model;

public class MarkSheetData {
    public String courseCode;
    public String courseDesc;
    public String mark;
    public String gpa;
    public String grade;

    public MarkSheetData(String courseCode, String courseDesc, String mark, String gpa, String grade) {
        this.courseCode = courseCode;
        this.courseDesc = courseDesc;
        this.mark = mark;
        this.gpa = gpa;
        this.grade = grade;
    }
}
