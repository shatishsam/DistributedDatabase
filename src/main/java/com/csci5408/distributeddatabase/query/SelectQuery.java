package com.csci5408.distributeddatabase.query;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends Query {

    private String tableName;
    private Criteria criteria;
    private List<String> columns;

    public SelectQuery(String sql, String tableName) {
        super(QueryType.SELECT, sql);
        this.tableName = tableName;
        this.columns = new ArrayList<>();
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

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void addColumn(String column) {
        this.columns.add(column);
    }

    @Override
    public String toString() {
        return "SelectQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", tableName='" + tableName + '\'' +
                ", criteria=" + criteria +
                ", columns=" + columns +
                '}';
    }
}
