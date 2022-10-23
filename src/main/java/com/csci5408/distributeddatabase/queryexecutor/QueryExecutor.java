package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.analytics.AnalyticsUtil;
import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.*;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.user.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class QueryExecutor {
    // query parser
    private QueryParser queryParser;
    private AnalyticsUtil analyticsUtil;
    private String sqlQuery;

    public QueryExecutor() {
    }

    // constructor
    public QueryExecutor(String sqlQuery) {
        this.sqlQuery = sqlQuery;
        this.analyticsUtil=new AnalyticsUtil();
    }

    public String executeQuery()
    {
        try
        {
            long startTime = System.currentTimeMillis();
            IQueryExecutor queryExecutor = null;
            QueryParser parser = new QueryParser();
            Query query = parser.parse(sqlQuery);
            switch (query.getQueryType()) {
                case CREATE_DATABASE:
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    queryExecutor = new CreateDatabaseExecutor(creatDatabaseQuery);
                    this.analyticsUtil.addAnalytics(creatDatabaseQuery);
                    break;
                case INSERT:
                    InsertQuery insertQuery = (InsertQuery) query;
                    queryExecutor = new InsertTableQueryExecutor(insertQuery);

                    this.analyticsUtil.addAnalytics(insertQuery);
                    break;
                case CREATE_TABLE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else {
                        CreateTableQuery createTableQuery = (CreateTableQuery) query;
                        queryExecutor = new CreateTableExecutor(createTableQuery, QueryExecutorUtil.getChosenDatabase());

                        this.analyticsUtil.addAnalytics(createTableQuery);
                    }
                    break;
                case UPDATE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        UpdateQuery updateQuery = (UpdateQuery) query;
                        queryExecutor = new UpdateQueryExecutor(updateQuery);

                        this.analyticsUtil.addAnalytics(updateQuery);
                    }
                    break;
                case DELETE:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        DeleteQuery deleteQuery = (DeleteQuery) query;
                        queryExecutor = new DeleteQueryExecutor(deleteQuery);

                        this.analyticsUtil.addAnalytics(deleteQuery);
                    }
                    break;
                case SELECT:
                    if (!QueryExecutorUtil.isDatabaseChosen())
                        return "No Database has been chosen please choose a database";
                    else{
                        SelectQuery selectQuery = (SelectQuery) query;
                        queryExecutor = new SelectQueryExecutor(selectQuery);

                        this.analyticsUtil.addAnalytics(selectQuery);
                    }
                    break;
                case USE:
                    UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery) query;
                    queryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);

                    this.analyticsUtil.addAnalytics(useDatabaseQuery);
                    break;
                default:
                    Logger.eventLogger(query+" "+"QUERY FAILED");
                    System.err.println("You have entered an invalid query");
            }
            long executionTime = System.currentTimeMillis()-startTime;
            Logger.generalLogger("Execution time for query "+query+"is"+executionTime);
            return queryExecutor.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "returned after executing normal query";
    }

    public String executeTransaction(String transactionQuery) throws Exception
    {

        String chosenDatabaseName = QueryExecutorUtil.getChosenDatabase();
        DistributedHelper distributedHelper = new DistributedHelper();

        if(distributedHelper.isDatabasePresentInOtherInstance(chosenDatabaseName))
        {
            System.err.println("routing transaction to other instance as db is present there");
            return distributedHelper.executeTransactionInOtherInstance(transactionQuery);
        }

        queryParser = new QueryParser();
        // splitting the query on the basis of ';'
        List<String> queryList = Arrays.asList(transactionQuery.split("(?<=;)"));
        Transaction transaction = new Transaction();
        if(queryList.get(0).equalsIgnoreCase("start transaction;") && queryList.get(queryList.size()-1).trim().equalsIgnoreCase("commit;")) {
            // iterating over the query list
            for (String query : queryList) {
                // trimming the query
                query = query.trim();
                // if it is not "start" and "commit" transaction query then add it into a queue
                if (!query.equalsIgnoreCase("start transaction;") && !query.equalsIgnoreCase("commit;")) {
                    transaction.addQuery(queryParser.parse(query));
                }
            }
        } else {
            Logger.eventLogger("::::::::::::::::TRANSACTION FAILED::::::::::::::::::::");
            return "Entered query is not a transaction";
        }

        //ToDo handle transaction queries here
        Queue<Query> queries = transaction.getQueries();
        IQueryExecutor executor = null;
        transaction.setDatabaseName(QueryExecutorUtil.getChosenDatabase());
        initializeTableInTransaction(transaction);
        for (Query query : queries) {
            queryParser.validateQuery(query, transaction);
        }

        boolean isQuerySuccessfullyExecuted=false;
        for (Query query : queries)
        {
            isQuerySuccessfullyExecuted = executeQueryForTransactions(query, transaction);
            if(!isQuerySuccessfullyExecuted) {
                System.out.println("::::::::::::::::::Transaction rolled back::::::::::::::");
                break;
            }
        }
        if(isQuerySuccessfullyExecuted) {
            for (Map.Entry<String, ArrayList> tableData : transaction.getTransactionalTableData().entrySet()) {
                String Path = System.getProperty("user.dir") + File.separator + transaction.getDatabaseName() + File.separator + tableData.getKey() + ".txt";
                FileUtil.writeTableHashMapToFile(tableData.getValue(), Path);
            }
        }
        return "returned after executing transaction query";
    }

    private boolean executeQueryForTransactions(Query query, Transaction transaction) throws Exception {
        if (query.getQueryType() == QueryType.CREATE_DATABASE) {
              new CreateDatabaseExecutor((CreatDatabaseQuery) query).execute();
              return true;
        } else if (query.getQueryType() == QueryType.CREATE_TABLE) {
            new CreateTableExecutor((CreateTableQuery) query, QueryExecutorUtil.getChosenDatabase()).execute();
            return true;
        } else if (query.getQueryType() == QueryType.INSERT) {
            ITransactionExecutor transactionExecutor = new InsertTableQueryExecutor((InsertQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.SELECT) {
            new SelectQueryExecutor((SelectQuery) query).execute();
            return true;
        } else if (query.getQueryType() == QueryType.USE) {
            new UseDatabaseQueryExecutor((UseDatabaseQuery) query).execute();
            return true;
        } else if (query.getQueryType() == QueryType.UPDATE) {
            ITransactionExecutor transactionExecutor = new UpdateQueryExecutor((UpdateQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        } else if (query.getQueryType() == QueryType.DELETE) {
            ITransactionExecutor transactionExecutor = new DeleteQueryExecutor((DeleteQuery) query);
            return transactionExecutor.executeTransaction(transaction);
        }
        Logger.eventLogger("::::::::::::::::TRANSACTION FAILED::::::::::::::::::::");
        throw new IllegalArgumentException("Oops query executor not found!!");
    }

    private void initializeTableInTransaction(Transaction transaction) throws IOException {
        Map<String, Properties> prop = new HashMap<>();
        Map<String, ArrayList> tableData = new HashMap<>();
        File file = new File(LocalMetaDataHandler.getDatabaseMetadataFolderPath(transaction.getDatabaseName()));
        File[] fileList = file.listFiles();
        String tableName;

        for (File list : fileList) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(list));
            System.out.println("printing list of files of table::::::::::::::"+list.getName());
            tableName = list.getName().replace("properties", "").replace(".", "");
            System.out.println("printing name of file::::::::::::::"+tableName);
            prop.put(tableName, properties);
            ArrayList tableStructure = TableStructureHelper.getTableStructure(transaction.getDatabaseName(), tableName);
            tableData.put(tableName, tableStructure);
        }
        transaction.setTransactionalTableProp(prop);
        transaction.setTransactionalTableData(tableData);
    }
}
