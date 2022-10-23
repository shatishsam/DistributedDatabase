package com.csci5408.distributeddatabase.query;

public class UseDatabaseQuery extends Query {
    private String databaseName;

    public UseDatabaseQuery(String sql, String databaseName) {
        super(QueryType.USE, sql);
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
        return "UseDatabaseQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }
}
