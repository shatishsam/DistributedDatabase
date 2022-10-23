package com.csci5408.distributeddatabase.parsers;

import com.csci5408.distributeddatabase.query.InsertQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.SelectQuery;
import com.csci5408.distributeddatabase.query.parsers.QueryParser;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testCreateDatabase() {
        String sql = "create database demo;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUseDatabase() {
        String sql = "use demo;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateTable() {
        String sql = "create table persons(id int, name varchar(255), primary key (id), foreign key (id) references orders(id);";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertQuery() {
        String sql = "insert into persons(id, name) values (1, janvi);";
        QueryParser parser = new QueryParser();
        try {
            InsertQuery query = (InsertQuery)parser.parse(sql);
            System.out.println(query.getFieldValueMap());
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteQuery() {
        String sql = "delete from person where personame!=janvi;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateQuery() {
        String sql = "update person set name=janvi where id=1;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectQuery() {
        String sql = "select * from person where name=janvi;";
        QueryParser parser = new QueryParser();
        try {
            SelectQuery query = (SelectQuery) parser.parse(sql);
            System.err.println(query.getColumns());
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Test
    public void testSelectSpecificQuery() {
        String sql = "select name, id from person;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectAllQuery() {
        String sql = "select * from person;";
        QueryParser parser = new QueryParser();
        try {
            Query query = parser.parse(sql);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}