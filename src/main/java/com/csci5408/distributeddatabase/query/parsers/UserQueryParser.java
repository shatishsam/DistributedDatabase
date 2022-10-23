package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.UseDatabaseQuery;

import java.util.List;

public class UserQueryParser extends Parser {

    public UserQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        if(sqlQueryParts.size()!=2) {
            throw new Exception("Oops!! Invalid use database query syntax");
        }
        String databaseName = sqlQueryParts.get(1);
        databaseName = databaseName.replace(";", "");
        return new UseDatabaseQuery(sqlQuery, databaseName);
    }
}
