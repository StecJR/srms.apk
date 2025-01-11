package com.stec.srms.model;

public class PendingVerifications {
    public Integer pvId;
    public Integer accountId;
    public Integer userId;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE pending_verifications")
                .append(" (")
                .append("pvId INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("accountId INTEGER NOT NULL, ")
                .append("userId INTEGER NOT NULL, ")

                .append("FOREIGN KEY (accountId) REFERENCES account_types(accountId)")
                .append(")");
        return query.toString();
    }
}
