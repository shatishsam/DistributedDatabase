package com.csci5408.distributeddatabase.query;

import java.util.LinkedHashMap;

public class CreateTableQuery extends Query {

    private String tableName;
    private String primaryKey;
    private String foreignKey;
    private String referenceTable;
    private String referenceTableField;

    private LinkedHashMap<String, String> fieldMap;

    public CreateTableQuery(String sql, String tableName) {
        super(QueryType.CREATE_TABLE, sql);
        this.tableName = tableName;
        this.fieldMap = new LinkedHashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceTableField() {
        return referenceTableField;
    }

    public void setReferenceTableField(String referenceTableField) {
        this.referenceTableField = referenceTableField;
    }

    public LinkedHashMap<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(LinkedHashMap<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public void addField(String fieldName, String fieldType) {
        this.fieldMap.put(fieldName, fieldType);
    }

    @Override
    public String toString() {
        return "CreateTableQuery{" +
                "queryType=" + this.getQueryType() + '\'' +
                ", sqlQuery=" + this.getSql() + '\'' +
                ", tableName='" + tableName + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", foreignKey='" + foreignKey + '\'' +
                ", referenceTable='" + referenceTable + '\'' +
                ", referenceTableField='" + referenceTableField + '\'' +
                ", fieldMap=" + fieldMap +
                '}';
    }
}
