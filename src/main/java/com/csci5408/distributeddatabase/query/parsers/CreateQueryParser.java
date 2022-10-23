package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.query.CreateTableQuery;
import com.csci5408.distributeddatabase.query.Query;

import java.util.Arrays;
import java.util.List;

public class CreateQueryParser extends Parser {

    public CreateQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        if (sqlQueryParts.get(1).equalsIgnoreCase("database")) {
            String databaseName = sqlQueryParts.get(2).substring(0,sqlQueryParts.get(2).length()-1);
            return new CreatDatabaseQuery(sqlQuery, databaseName);
        } else if (sqlQueryParts.get(1).equalsIgnoreCase("table")) {
            String tableName = sqlQueryParts.get(2);
            int i = tableName.indexOf("(");
            tableName = tableName.substring(0, i).trim();
            i = sqlQuery.indexOf("(");
            String properties = sqlQuery.substring(i + 1, sqlQuery.length() - 2);
            List<String> fields = Arrays.asList(properties.split(","));

            CreateTableQuery query = new CreateTableQuery(sqlQuery, tableName);

            for (String field : fields) {
                field = field.trim();
                List<String> individualField = Arrays.asList(field.split("\\s+"));
                if (individualField.get(0).equalsIgnoreCase("primary")) {
                    String primaryKey = individualField.get(2).replaceAll("[\\(\\)]", "");
                    query.setPrimaryKey(primaryKey);
                    continue;
                } else if (individualField.get(0).equalsIgnoreCase("foreign")) {
                    String foreignKey = individualField.get(2).replaceAll("[\\(\\)]", "");
                    query.setForeignKey(foreignKey);
                    String references = individualField.get(4);
                    i = references.indexOf("(");
                    query.setReferenceTable(references.substring(0, i).trim());
                    query.setReferenceTableField(references.substring(i + 1));
                    continue;
                }
                query.addField(individualField.get(0), individualField.get(1));
            }
            return query;
        } else {
            throw new Exception("Oops!! Cannot parse the create database query");
        }
    }
}
