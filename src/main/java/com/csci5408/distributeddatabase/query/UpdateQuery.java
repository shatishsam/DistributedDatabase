package com.csci5408.distributeddatabase.query;

public class UpdateQuery extends Query {

    private String tableName;
    private String columnName;
    private String updatedColumnValue;
    private Criteria criteria;

    public UpdateQuery(String sql, String tableName, String columnName, String updatedColumnValue) {
        super(QueryType.UPDATE, sql);
        this.tableName = tableName;
        this.columnName = columnName;
        this.updatedColumnValue = updatedColumnValue;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getUpdatedColumnValue() {
        return updatedColumnValue;
    }

    public void setUpdatedColumnValue(String updatedColumnValue) {
        this.updatedColumnValue = updatedColumnValue;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "UpdateQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", updatedColumnValue='" + updatedColumnValue + '\'' +
                ", criteria=" + criteria +
                '}';
    }
}
