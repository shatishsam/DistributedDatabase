package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.query.UseDatabaseQuery;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.QueryExecutor;
import com.csci5408.distributeddatabase.queryexecutor.UseDatabaseQueryExecutor;
import org.junit.Test;

public class TransactionExecutorTest
{
    @Test
    public void getTransactionFailureTest() throws Exception {
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("use employee; select * from employee; create table customer(id number, name varchar);");
    }

    @Test
    public void getTransactionSuccessTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("start transaction; select * from employee; create table customer(id number, name varchar); commit;");
    }

    @Test
    public void getCreateTableTransactionTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("start transaction; create table customer(id number, name varchar); create table customer(id number, name varchar); commit;");
    }

    @Test
    public void updateQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("start transaction; update persons set name=kinna where name=vipul; commit;");
    }

    @Test
    public void insertQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("start transaction; insert into persons(id, name, lastname) values (10, ms, dhoni); select * from persons where name=janvi; commit;");

    }

    @Test
    public void deleteQueryTableTest() throws Exception {

        //useDatabase
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.executeTransaction("start transaction;  insert into persons(id, name, lastname) values (8, vipul, patel); delete from persons where name=janvi; commit;");

    }

}
