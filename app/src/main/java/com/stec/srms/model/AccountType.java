package com.stec.srms.model;

public class AccountType {
    public int accountId;
    public String accountType;

    public AccountType() {
    }

    public AccountType(int accountId, String accountType) {
        this.accountId = accountId;
        this.accountType = accountType;
    }

    public static String getQuery() {
        return "CREATE TABLE account_types" +
                " (" +
                "accountId INTEGER PRIMARY KEY, " +
                "accountType TEXT" +
                ");";
    }
}
