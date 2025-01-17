package com.stec.srms.model;

public class AccountType {
    public int accountId;
    public String accountType;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE account_types")
                .append(" (")
                .append("accountId INTEGER PRIMARY KEY, ")
                .append("accountType TEXT")
                .append(");");
        return query.toString();
    }

    public AccountType() {
    }

    public AccountType(int accountId, String accountType) {
        this.accountId = accountId;
        this.accountType = accountType;
    }
}
