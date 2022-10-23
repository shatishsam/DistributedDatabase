package com.csci5408.distributeddatabase.analytics;

import com.csci5408.distributeddatabase.query.CreatDatabaseQuery;
import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import com.csci5408.distributeddatabase.util.FileUtil;

import static com.csci5408.distributeddatabase.query.QueryType.*;

public class AnalyticsUtil
{
    private static UserAnalyticsUtil userAnalyticsUtil;

    private static QueryAnalyticsUtil queryAnalyticsUtil;

    public AnalyticsUtil()
    {
        userAnalyticsUtil = new UserAnalyticsUtil();
        queryAnalyticsUtil = new QueryAnalyticsUtil();
    }

    public boolean addAnalytics(Query query)
    {
        addAnayticsForQueryType(query);
        addAnalyticsForUserType(query);
        return true;
    }

    private boolean addAnayticsForQueryType(Query query)
    {
        if(query.getQueryType().equals(CREATE_TABLE) || query.getQueryType().equals(CREATE_DATABASE))
        {
            queryAnalyticsUtil.incrementCreateAnalayticsCount();
        }
        else if(query.getQueryType().equals(UPDATE))
        {
            queryAnalyticsUtil.incrementUpdateAnalayticsCount();
        }
        else if(query.getQueryType().equals(DELETE))
        {
            queryAnalyticsUtil.incrementDeleteAnalayticsCount();
        }
        else if(query.getQueryType().equals(SELECT))
        {
            queryAnalyticsUtil.incrementSelectAnalayticsCount();
        }
        return true;
    }

    private boolean addAnalyticsForUserType(Query query)
    {
        System.err.println(System.getProperties());
        System.err.println("adding user logs for query "+query+" lis logged in user contains? "+System.getProperties().containsKey(UserAnalyticsConstants.LOGGED_IN_USER_NAME));
        if(System.getProperties().containsKey(UserAnalyticsConstants.LOGGED_IN_USER_NAME))
        {
            try
            {
                //step 1 increment total query count
                userAnalyticsUtil.incrementTotalQueriesProp();

                //step2 increment db count
                String userName = System.getProperties().getProperty(UserAnalyticsConstants.LOGGED_IN_USER_NAME);
                String databaseName = "";

                if(QueryExecutorUtil.isDatabaseChosen())
                {
                    databaseName=QueryExecutorUtil.getChosenDatabase();
                }
                else if(query.getQueryType().equals(CREATE_DATABASE))
                {
                    CreatDatabaseQuery creatDatabaseQuery = (CreatDatabaseQuery) query;
                    databaseName=creatDatabaseQuery.getDatabaseName();
                }

                String propName = userName+"_"+databaseName;
                userAnalyticsUtil.incrementAnalyticsProp(propName);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public String getAllAnalyticsData()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(queryAnalyticsUtil.getAnalyticsData()).append("\n");
        sb.append(userAnalyticsUtil.getAnalyticsData()).append("\n");
        return sb.toString();
    }

}
