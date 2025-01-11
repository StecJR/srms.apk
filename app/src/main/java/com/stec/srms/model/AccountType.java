package com.stec.srms.model;

public class AccountType {
    public Integer accountId;
    public String accountType;

    public static String getQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE account_types")
                .append(" (")
                .append("accountId INTEGER, ")
                .append("accountType TEXT, ")

                .append("PRIMARY KEY (accountId)")
                .append(")");
        return query.toString();
    }
}
