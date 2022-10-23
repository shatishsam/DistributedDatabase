package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.query.CreateTableQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.Map;
import java.util.Properties;

public class CreateTableQueryValidator implements Validator {

    @Override
    public void validate(Query query, Transaction transaction) throws Exception {
        CreateTableQuery createTableQuery = (CreateTableQuery) query;
        String databaseName = QueryExecutorUtil.getChosenDatabase();
        if (QueryExecutorUtil.isTableExistsInDatabase(databaseName, createTableQuery.getTableName())) {
            throw new IllegalArgumentException("OOPS!! Table already exists");
        }
        if (transaction != null) {
            if (transaction.getTransactionalTableProp().containsKey(createTableQuery.getTableName())) {
                throw new IllegalArgumentException("OOPS!! Table already exists");
            }
            Properties properties = new Properties();
            String primaryKey = createTableQuery.getPrimaryKey() == null ? "" : createTableQuery.getPrimaryKey();
            String foreignKey = createTableQuery.getForeignKey() == null ? "" : createTableQuery.getForeignKey();
            String refTable = createTableQuery.getReferenceTable() == null ? "" : createTableQuery.getReferenceTable();
            String refTableField = createTableQuery.getReferenceTableField() == null ? "" : createTableQuery.getReferenceTableField();
            properties.setProperty("primaryKey", primaryKey);
            properties.setProperty("foreignKey", foreignKey);
            properties.setProperty("referenceTable", refTable);
            properties.setProperty("referenceTableField", refTableField);
            for (Map.Entry<String, String> entrySet : createTableQuery.getFieldMap().entrySet()) {
                properties.setProperty(entrySet.getKey(), entrySet.getValue());
            }
            transaction.getTransactionalTableProp().put(createTableQuery.getTableName(), properties);
        }
    }
}
