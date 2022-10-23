package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.query.UpdateQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.user.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UpdateQueryExecutor implements IQueryExecutor, ITransactionExecutor {

    private String tableName;
    private String column;
    private String updatedColumnValue;
    private String whereColumn;
    private String whereValue;
    private String operator;
    private UpdateQuery updateQuery;
    private ArrayList<LinkedHashMap<String, String>> tableData;

    public UpdateQueryExecutor(UpdateQuery updateQuery) {
        this.updateQuery = updateQuery;
        tableName = updateQuery.getTableName();
        column = updateQuery.getColumnName();
        updatedColumnValue = updateQuery.getUpdatedColumnValue();
        whereColumn = updateQuery.getCriteria().getLeftOperand();
        whereValue = updateQuery.getCriteria().getRightOperand();
        operator = updateQuery.getCriteria().getOperator();
    }

    @Override
    public String execute() throws Exception {

        StringBuilder result = new StringBuilder();
        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        tableName = updateQuery.getTableName();

        DistributedHelper distributedHelper = new DistributedHelper();
        if(!distributedHelper.isDatabasePresentInLocalInstance(chosenDatabaseName))
        {
            result.append(distributedHelper.executeQueryInOtherInstance(this.updateQuery.getSql()));
            return result.toString();
        }

        if (QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName))
        {
            tableData = TableStructureHelper.getTableStructure(chosenDatabaseName, tableName);
            updateQueryOnDataStructure();
            String path= System.getProperty("user.dir")+ File.separator+chosenDatabaseName+File.separator+tableName+".txt";
            FileUtil.writeTableHashMapToFile(tableData, path);
            result.append("update changes happened successfully in table");
        }
        else
        {
            result.append("Table does not exists in the instance");
        }
        Logger.queryLogger("::::::::::::::::UPDATE query executed::::::::::::::::::::");
        return result.toString();
    }

    private void updateQueryOnDataStructure()
    {
        for (HashMap<String, String> eachTableData : tableData) {
            if (operator.equalsIgnoreCase("=")) {
                if (eachTableData.keySet().contains(whereColumn)) {
                    if (eachTableData.get(whereColumn).equalsIgnoreCase(whereValue)) {
                        eachTableData.put(column, updatedColumnValue);
                    }
                }
            }
            if (operator.equalsIgnoreCase("<=")) {
                if (Integer.parseInt(eachTableData.get(whereColumn)) <= Integer.parseInt(whereValue)) {
                    eachTableData.put(column, updatedColumnValue);
                }
            }
            if (operator.equalsIgnoreCase(">=")) {
                if (Integer.parseInt(eachTableData.get(whereColumn)) >= Integer.parseInt(whereValue)) {
                    eachTableData.put(column, updatedColumnValue);
                }
            }
        }
    }

    @Override
    public boolean executeTransaction(Transaction transaction)
    {
        try
        {
            String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
            if (QueryExecutorUtil.isTableExistsInDatabase(chosenDatabaseName, tableName))
            {
                tableData = transaction.getTransactionalTableData().get(updateQuery.getTableName());
                updateQueryOnDataStructure();
                Logger.queryLogger("::::::::::::::::UPDATE query in transaction executed::::::::::::::::::::");
                return true;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}

