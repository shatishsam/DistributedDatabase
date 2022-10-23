package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.Criteria;
import com.csci5408.distributeddatabase.query.DeleteQuery;
import com.csci5408.distributeddatabase.query.Query;

import java.util.List;

public class DeleteQueryParser extends Parser {

    public DeleteQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        if (!sqlQueryParts.get(0).equalsIgnoreCase("delete")) {
            throw new Exception("Oops!! delete query syntax is invalid!");
        }

        int fromKeyword = sqlQueryParts.indexOf("from");

        if (fromKeyword == -1) {
            throw new Exception("Oops!! delete query syntax is invalid!");
        }

        String tableName = sqlQueryParts.get(fromKeyword + 1).replace(";", "");
        DeleteQuery query = new DeleteQuery(sqlQuery, tableName);

        int whereIndex = sqlQueryParts.indexOf("where");
        if (whereIndex != -1) {
            String whereCondition = sqlQueryParts.get(whereIndex + 1);
            for (String operator : operators) {
                if (whereCondition.contains(operator)) {
                    String[] whereConditionParse = whereCondition.split(operator);
                    query.setCriteria(new Criteria(whereConditionParse[0], whereConditionParse[1].replaceAll(";", ""), operator));
                    break;
                }
            }
        }
        return query;
    }
}
