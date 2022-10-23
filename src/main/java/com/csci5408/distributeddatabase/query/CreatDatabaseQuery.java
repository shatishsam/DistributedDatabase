package com.csci5408.distributeddatabase.query;

public class CreatDatabaseQuery extends Query {

    private String databaseName;

    public CreatDatabaseQuery(String sql, String databaseName) {
        super(QueryType.CREATE_DATABASE, sql);
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return "CreatDatabaseQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }
}
