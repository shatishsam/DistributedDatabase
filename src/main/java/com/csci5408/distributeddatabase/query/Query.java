package com.csci5408.distributeddatabase.query;

public class Query {

    private QueryType queryType;
    private String sql;

    public Query(QueryType queryType, String sql) {
        this.queryType = queryType;
        this.sql = sql;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "Query{" +
                "queryType=" + queryType +
                ", sql='" + sql + '\'' +
                '}';
    }
}
