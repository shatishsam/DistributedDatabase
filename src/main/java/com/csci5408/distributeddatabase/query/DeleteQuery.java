package com.csci5408.distributeddatabase.query;

public class DeleteQuery extends Query {

    private String tableName;
    private Criteria criteria;

    public DeleteQuery(String sql, String tableName) {
        super(QueryType.DELETE, sql);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "DeleteQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", tableName='" + tableName + '\'' +
                ", criteria=" + criteria +
                '}';
    }
}
