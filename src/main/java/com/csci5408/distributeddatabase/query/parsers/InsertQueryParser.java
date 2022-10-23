package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.InsertQuery;
import com.csci5408.distributeddatabase.query.Query;

import java.util.Arrays;
import java.util.List;

public class InsertQueryParser extends Parser{
    public InsertQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        int i, j;
        if(!sqlQueryParts.get(1).equalsIgnoreCase("into")) {
            throw new Exception("Oops!!, insert query is not valid");
        }
        String tableName = sqlQueryParts.get(2);
        i = tableName.indexOf("(");
        tableName = tableName.substring(0, i > 0 ? i : tableName.length());

        i = sqlQuery.indexOf("(");
        j = sqlQuery.indexOf(")");

        if(i == -1 || j == -1) {
            throw new Exception("Oops!!, insert query syntax is wrong! Try again!");
        }

        String fields = sqlQuery.substring(i+1, j).trim();

        String values = sqlQuery.substring(j+1);
        i = values.indexOf("(");
        j = values.indexOf(")");

        String keyword = values.substring(0, i);

        if(!keyword.trim().equalsIgnoreCase("values")) {
            throw new Exception("Oops!!, insert query syntax is wrong! Try again!");
        }

        values = values.substring(i+1, j).trim();

        List<String> fieldList = Arrays.asList(fields.split(","));
        List<String> valueList = Arrays.asList(values.split(","));

        if(fieldList.size() != valueList.size()) {
            throw new Exception("Oops!!, insert query syntax is wrong! Try again!");
        }

        String field = "";
        String value = "";

        InsertQuery query = new InsertQuery(sqlQuery, tableName);

        for(int f = 0; f < fieldList.size(); f++) {
            field = fieldList.get(f).trim();
            value = valueList.get(f).replace("\"", "").trim();
            query.addFieldValue(field, value);
        }
        return query;
    }
}
