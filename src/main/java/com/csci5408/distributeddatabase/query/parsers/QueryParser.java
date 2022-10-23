package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.validator.*;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;
import com.csci5408.distributeddatabase.user.Logger;

import java.util.Arrays;
import java.util.List;

public class QueryParser {

    public Query parse(String sqlQuery) throws Exception {
        sqlQuery = sqlQuery.trim();
        if (!sqlQuery.substring(sqlQuery.length() - 1).equalsIgnoreCase(";")) {
            throw new Exception("Invalid Query Syntax. Semi-Colon is Missing !");
        }

        List<String> sqlQueryParts = Arrays.asList(sqlQuery.split("\\s+"));

        String queryType = sqlQueryParts.get(0);

        Parser parser;

        switch (queryType) {
            case "create":
                parser = new CreateQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "use":
                parser = new UserQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "insert":
                parser = new InsertQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "delete":
                parser = new DeleteQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "update":
                parser = new UpdateQueryParser(sqlQuery, sqlQueryParts);
                break;

            case "select":
                parser = new SelectQueryParser(sqlQuery, sqlQueryParts);
                break;

            default:
                Logger.eventLogger(sqlQuery+"     QUERY FAILED");
                throw new Exception("Oops!! Query Type is Invalid");
        }
        return parser.parse();
    }

    public void validateQuery(Query query, Transaction transaction) throws Exception {
        switch (query.getQueryType()) {
            case CREATE_DATABASE:
                new CreateDatabaseQueryValidator().validate(query, transaction);
                break;

            case USE:
                new UseDatabaseQueryValidator().validate(query,transaction);
                break;

            case CREATE_TABLE:
                new CreateTableQueryValidator().validate(query, transaction);
                break;

            case INSERT:
                new InsertQueryValidator().validate(query, transaction);
                break;

            case SELECT:
                new SelectQueryValidator().validate(query, transaction);
                break;

            case UPDATE:
                new UpdateTableQueryValidator().validate(query, transaction);
                break;

            case DELETE:
                new DeleteQueryValidator().validate(query, transaction);
                break;

            default:
                Logger.eventLogger(query+"     QUERY FAILED");
                throw new Exception("Oops!! Query Type is Invalid");
        }
    }
}
