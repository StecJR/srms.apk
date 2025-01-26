package com.stec.srms.model;

public class PendingVerifications {
    public int pvId;
    public int accountId;
    public int userId;

    public PendingVerifications() {}
    public PendingVerifications(int pvId, int accountId, int userId) {
        this.pvId = pvId;
        this.accountId = accountId;
        this.userId = userId;
    }

    public static String getQuery() {
        return "CREATE TABLE pending_verifications" +
                " (" +
                "pvId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "accountId INTEGER, " +
                "userId INTEGER, " +
                "FOREIGN KEY (accountId) REFERENCES account_types(accountId)" +
                ");";
    }
}
