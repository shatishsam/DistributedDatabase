package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.DeleteQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.Properties;

public class DeleteQueryValidator implements Validator {
    @Override
    public void validate(Query query, Transaction transaction) throws Exception {
        DeleteQuery deleteQuery = (DeleteQuery) query;
        String databaseName = QueryExecutorUtil.getChosenDatabase();

        if (!QueryExecutorUtil.isTableExistsInDatabase(databaseName, deleteQuery.getTableName()) && transaction == null) {
            throw new IllegalArgumentException("OOPS!! Table doesn't exists");
        }
        Properties tableMetadataProp;
        if (transaction == null) {
            LocalMetaDataHandler localMetaDataHandler = new LocalMetaDataHandler();
            tableMetadataProp = localMetaDataHandler.getTableMetadataProp(databaseName, deleteQuery.getTableName());
        } else {
            tableMetadataProp = transaction.getTransactionalTableProp().get(deleteQuery.getTableName());
            if (tableMetadataProp == null) {
                throw new IllegalArgumentException("OOPS!! Table doesn't exists");
            }
        }

        if (deleteQuery.getCriteria() != null && !tableMetadataProp.containsKey(deleteQuery.getCriteria().getLeftOperand())) {
            throw new Exception("Criteria column you are trying to delete doesn't exist!");
        }

        if (deleteQuery.getCriteria() != null) {
            String type = tableMetadataProp.getProperty(deleteQuery.getCriteria().getLeftOperand());
            if ("int".equalsIgnoreCase(type) && !QueryExecutorUtil.isColumnInteger(deleteQuery.getCriteria().getRightOperand())) {
                throw new Exception("Criteria column you are trying to delete doesn't have same type!");
            }
        }
    }
}
