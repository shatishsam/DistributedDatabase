package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.query.Query;

import java.util.*;

public class Transaction {
    // Queue of queries
    private Queue<Query> queries;
    private String databaseName;
    private Map<String, Properties> transactionalTableProp;
    private Map<String, ArrayList> transactionalTableData;

    // constructor
    public Transaction() {
        this.queries = new LinkedList<>();
    }

    // getting the queries
    public Queue<Query> getQueries() {
        return queries;
    }

    // setting the queries
    public void setQueries(Queue<Query> queries) {
        this.queries = queries;
    }

    // adding the queries
    public void addQuery(Query query) {
        this.queries.add(query);
    }

    public Map<String, Properties> getTransactionalTableProp() {
        return transactionalTableProp;
    }

    public void setTransactionalTableProp(Map<String, Properties> transactionalTableProp) {
        this.transactionalTableProp = transactionalTableProp;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Map<String, ArrayList> getTransactionalTableData() {
        return transactionalTableData;
    }

    public void setTransactionalTableData(Map<String, ArrayList> transactionalTableData) {
        this.transactionalTableData = transactionalTableData;
    }
}
