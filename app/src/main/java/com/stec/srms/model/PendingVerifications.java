package com.stec.srms.model;

public class PendingVerifications {
    public int pvId;
    public int accountId;
    public int userId;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE pending_verifications")
                .append(" (")
                .append("pvId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("accountId INTEGER, ")
                .append("userId INTEGER, ")

                .append("FOREIGN KEY (accountId) REFERENCES account_types(accountId)")
                .append(");");
        return query.toString();
    }

    public PendingVerifications() {
    }

    public PendingVerifications(int pvId, int accountId, int userId) {
        this.pvId = pvId;
        this.accountId = accountId;
        this.userId = userId;
    }
}
