package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.UpdateQuery;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.Properties;

public class UpdateTableQueryValidator implements Validator {

    @Override
    public void validate(Query query, Transaction transaction) throws Exception {
        UpdateQuery updateQuery = (UpdateQuery) query;
        String databaseName = QueryExecutorUtil.getChosenDatabase();

        if (!QueryExecutorUtil.isTableExistsInDatabase(databaseName, updateQuery.getTableName()) && transaction == null) {
            throw new IllegalArgumentException("OOPS!! Table doesn't exists");
        }
        Properties tableMetadataProp;
        if (transaction == null) {
            LocalMetaDataHandler localMetaDataHandler = new LocalMetaDataHandler();
            tableMetadataProp = localMetaDataHandler.getTableMetadataProp(databaseName, updateQuery.getTableName());
        } else {
            tableMetadataProp = transaction.getTransactionalTableProp().get(updateQuery.getTableName());
            if (tableMetadataProp == null) {
                throw new IllegalArgumentException("OOPS!! Table doesn't exists");
            }
        }

        if (!tableMetadataProp.containsKey(updateQuery.getColumnName())) {
            throw new Exception("Column you are trying to update doesn't exist!");
        }

        if ("int".equalsIgnoreCase(tableMetadataProp.getProperty(updateQuery.getColumnName())) && !QueryExecutorUtil.isColumnInteger(updateQuery.getUpdatedColumnValue())) {
            throw new Exception("Column you are trying to update doesn't have same type!");
        }

        if (updateQuery.getCriteria() != null && !tableMetadataProp.containsKey(updateQuery.getCriteria().getLeftOperand())) {
            throw new Exception("Criteria column you are trying to update doesn't exist!");
        }

        if (updateQuery.getCriteria() != null) {
            String type = tableMetadataProp.getProperty(updateQuery.getCriteria().getLeftOperand());
            if ("int".equalsIgnoreCase(type) && !QueryExecutorUtil.isColumnInteger(updateQuery.getCriteria().getRightOperand())) {
                throw new Exception("Criteria column you are trying to update doesn't have same type!");
            }
        }
    }
}