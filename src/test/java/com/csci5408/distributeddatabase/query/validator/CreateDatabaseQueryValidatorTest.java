package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.UseDatabaseQuery;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import com.csci5408.distributeddatabase.queryexecutor.UseDatabaseQueryExecutor;
import org.junit.Test;

public class CreateDatabaseQueryValidatorTest {

    @Test
    public void testCreateDatabaseQueryValidator() {
        String sql = "create database demo;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
            System.out.println("database doesn't exist, create database query validated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUseDatabaseQueryValidator() {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateTableQueryValidator() throws Exception {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();

        sql = "create table persons(id int, name varchar(255), lastname varchar(255), primary key (id), foreign key (id) references orders(id);";
        parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateQueryValidator() throws Exception {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();

        sql = "update persons set id=janvi where id=14;";
        parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertQueryValidator() throws Exception {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();

        sql = "insert into persons(id, name, lastname) values (1, bharat, padhiyar);";
        parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectQueryValidator() throws Exception {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();

        UseDatabaseQuery useDatabaseQuery = (UseDatabaseQuery)parser.parse(sql);
        UseDatabaseQueryExecutor useDatabaseQueryExecutor = new UseDatabaseQueryExecutor(useDatabaseQuery);
        useDatabaseQueryExecutor.execute();

        sql = "select name from persons where id=3;";
        parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println("Parsed Successfully");
            parser.validateQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}