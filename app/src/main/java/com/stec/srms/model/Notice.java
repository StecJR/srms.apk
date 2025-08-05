package com.stec.srms.model;

public class Notice {
    public int noticeId;
    public int targetUserId;
    public String title;
    public String description;
    public String createdAt;

    public Notice() {
    }

    public Notice(int noticeId, int targetUserId, String title, String description, String createdAt) {
        this.noticeId = noticeId;
        this.targetUserId = targetUserId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static String getQuery() {
        return "CREATE TABLE notice_board" +
                " (" +
                "noticeId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "targetUserId INTEGER, " +
                "title TEXT, " +
                "description TEXT, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
    }
}
