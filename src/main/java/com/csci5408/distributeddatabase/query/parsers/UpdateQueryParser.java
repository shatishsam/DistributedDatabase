package com.csci5408.distributeddatabase.query.parsers;

import com.csci5408.distributeddatabase.query.Criteria;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.query.UpdateQuery;

import java.util.List;

public class UpdateQueryParser extends Parser{

    public UpdateQueryParser(String sqlQuery, List<String> sqlQueryParts) {
        super(sqlQuery, sqlQueryParts);
    }

    @Override
    public Query parse() throws Exception {
        String tableName = sqlQueryParts.get(1);

        if (!sqlQueryParts.get(2).equalsIgnoreCase("set")) {
            throw new Exception("Oops!!, update query syntax is wrong! Try again!");
        }

        String[] updatedColumnValue = sqlQueryParts.get(3).split("=");
        if (updatedColumnValue.length != 2) {
            throw new Exception("Oops!!, update query syntax is wrong! Try again!");
        }

        UpdateQuery query = new UpdateQuery(sqlQuery, tableName, updatedColumnValue[0], updatedColumnValue[1]);

        int whereIndex = sqlQueryParts.indexOf("where");
        if (whereIndex != -1) {
            String whereCondition = sqlQueryParts.get(whereIndex+1).replace(";", "");
            for (String operator : operators) {
                if (whereCondition.contains(operator)) {
                    String[] whereConditionParse = whereCondition.split(operator);
                    if (whereConditionParse.length != 2) {
                        throw new Exception("Oops!!, update query syntax is wrong near a where clause! Try again!");
                    }
                    query.setCriteria(new Criteria(whereConditionParse[0], whereConditionParse[1], operator));
                    break;
                }
            }

            if (query.getCriteria() == null) {
                throw new Exception("Oops!!, update query syntax is wrong near a where clause! Try again!");
            }
        }

        return query;
    }
}
