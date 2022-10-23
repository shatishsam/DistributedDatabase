package com.csci5408.distributeddatabase.query;

import java.util.LinkedHashMap;

public class InsertQuery extends Query {

    private String tableName;
    private LinkedHashMap<String, String> fieldValueMap;

    public InsertQuery(String sql, String tableName) {
        super(QueryType.INSERT, sql);
        this.tableName = tableName;
        this.fieldValueMap = new LinkedHashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public LinkedHashMap<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(LinkedHashMap<String, String> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }

    public void addFieldValue(String field, String value) {
        this.fieldValueMap.put(field, value);
    }

    @Override
    public String toString() {
        return "InsertQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", tableName='" + tableName + '\'' +
                ", fieldValueMap=" + fieldValueMap +
                '}';
    }
}
