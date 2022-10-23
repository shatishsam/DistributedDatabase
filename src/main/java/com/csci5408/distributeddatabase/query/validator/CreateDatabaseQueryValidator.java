package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

public class CreateDatabaseQueryValidator implements Validator {
    @Override
    public void validate(Query query, Transaction transaction) {
        if (transaction != null) {
            throw new IllegalArgumentException("Cannot use create database inside transaction query");
        }
        CreatDatabaseQuery dbQuery = (CreatDatabaseQuery) query;
        if (QueryExecutorUtil.isDatabaseExistsInLocal(dbQuery.getDatabaseName())) {
            throw new IllegalArgumentException("OOPS!! Database already exists");
        }
    }
}
