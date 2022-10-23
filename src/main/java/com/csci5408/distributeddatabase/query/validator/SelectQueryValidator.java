package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.SelectQuery;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.util.List;
import java.util.Properties;

public class SelectQueryValidator implements Validator {
    @Override
    public void validate(Query query, Transaction transaction) throws Exception {
        SelectQuery selectQuery = (SelectQuery) query;
        String databaseName = QueryExecutorUtil.getChosenDatabase();

        if (!QueryExecutorUtil.isTableExistsInDatabase(databaseName, selectQuery.getTableName()) && transaction == null) {
            throw new IllegalArgumentException("OOPS!! Table doesn't exists");
        }

        Properties tableMetadataProp;
        if (transaction == null) {
            LocalMetaDataHandler localMetaDataHandler = new LocalMetaDataHandler();
            tableMetadataProp = localMetaDataHandler.getTableMetadataProp(databaseName, selectQuery.getTableName());
        } else {
            tableMetadataProp = transaction.getTransactionalTableProp().get(selectQuery.getTableName());
            if (tableMetadataProp == null) {
                throw new IllegalArgumentException("OOPS!! Table doesn't exists");
            }
        }

        List<String> fields = selectQuery.getColumns();

        if (!"*".equals(fields.get(0).trim())) {
            for (String field : fields) {
                if (!tableMetadataProp.containsKey(field)) {
                    throw new Exception("Column you are trying to select doesn't exist!");
                }
            }
        }
        if (selectQuery.getCriteria() != null && !tableMetadataProp.containsKey(selectQuery.getCriteria().getLeftOperand())) {
            throw new Exception("Criteria column you are trying to select doesn't exist!");
        }

        if (selectQuery.getCriteria() != null) {
            String type = tableMetadataProp.getProperty(selectQuery.getCriteria().getLeftOperand());
            if ("int".equalsIgnoreCase(type) && !QueryExecutorUtil.isColumnInteger(selectQuery.getCriteria().getRightOperand())) {
                throw new Exception("Criteria column you are trying to update doesn't have same type!");
            }
        }
    }
}
