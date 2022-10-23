package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.InsertQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.Properties;
import java.util.Set;

public class InsertQueryValidator implements Validator {
    @Override
    public void validate(Query query, Transaction transaction) throws Exception {
        InsertQuery insertQuery = (InsertQuery) query;
        String databaseName = QueryExecutorUtil.getChosenDatabase();

        if (!QueryExecutorUtil.isTableExistsInDatabase(databaseName, insertQuery.getTableName()) && transaction == null) {
            throw new IllegalArgumentException("OOPS!! Table doesn't exists");
        }
        Properties tableMetadataProp;

        if (transaction == null) {
            LocalMetaDataHandler localMetaDataHandler = new LocalMetaDataHandler();
            tableMetadataProp = localMetaDataHandler.getTableMetadataProp(databaseName, insertQuery.getTableName());
        } else {
            tableMetadataProp = transaction.getTransactionalTableProp().get(insertQuery.getTableName());
            if (tableMetadataProp == null) {
                throw new IllegalArgumentException("OOPS!! Table doesn't exists");
            }
        }

        Set<String> fields = insertQuery.getFieldValueMap().keySet();

        for (String field : fields) {
            if (!tableMetadataProp.containsKey(field)) {
                throw new Exception("Column you are trying to insert doesn't exist!");
            }
            if ("int".equalsIgnoreCase(tableMetadataProp.getProperty(field)) && !QueryExecutorUtil.isColumnInteger(insertQuery.getFieldValueMap().get(field))) {
                throw new Exception("Column you are trying to insert doesn't have same type!");
            }
        }
    }
}
