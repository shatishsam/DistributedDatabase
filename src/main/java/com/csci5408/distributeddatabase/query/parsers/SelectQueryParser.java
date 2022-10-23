package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.Criteria;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.SelectQuery;

import java.util.List;

public class SelectQueryParser extends Parser {

    public SelectQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        int fromKeywordIndex = sqlQueryParts.indexOf("from");
        if (fromKeywordIndex == -1) {
            throw new Exception("Oops!!, select query syntax is wrong! Try again!");
        }

        String tableName = sqlQueryParts.get(fromKeywordIndex + 1).replace(";", "");
        SelectQuery query = new SelectQuery(sqlQuery, tableName);

        int i = sqlQueryParts.indexOf("select") + 1;
        int j = fromKeywordIndex - 1;

        for (int index = i; index <= j; index++) {
            query.addColumn(sqlQueryParts.get(index).replace(",", ""));
        }

        if (query.getColumns().size() == 0) {
            throw new Exception("Oops!!, select query syntax is wrong near from clause! Try again!");
        }

        int whereIndex = sqlQueryParts.indexOf("where");
        if (whereIndex != -1) {
            String whereCondition = sqlQueryParts.get(whereIndex + 1).replace(";", "");
            for (String operator : operators) {
                if (whereCondition.contains(operator)) {
                    String[] whereConditionParse = whereCondition.split(operator);
                    if (whereConditionParse.length != 2) {
                        throw new Exception("Oops!!, select query syntax is wrong near a where clause! Try again!");
                    }

                    query.setCriteria(new Criteria(whereConditionParse[0], whereConditionParse[1], operator));
                    break;
                }
            }
        }

        return query;
    }
}
