package com.csci5408.distributeddatabase.analytics;

import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.util.PropertyUtil;

import java.nio.file.Path;
import java.util.Properties;

public class QueryAnalyticsUtil
{
    public QueryAnalyticsUtil()
    {
        init();
    }

    public void init()
    {
        if(!FileUtil.isFileExists(QueryAnalyticsConstants.ANALYTICS_FILE_NAME))
        {
            FileUtil.makeDirectory(QueryAnalyticsConstants.ANALYTICS_DIRECTORY_NAME);
            Properties analyticsProp = new Properties();
            analyticsProp.setProperty(QueryAnalyticsConstants.ANALYTICS_CREATE_QUERY, "0");
            analyticsProp.setProperty(QueryAnalyticsConstants.ANALYTICS_UPDATE_QUERY, "0");
            analyticsProp.setProperty(QueryAnalyticsConstants.ANALYTICS_DELETE_QUERY, "0");
            analyticsProp.setProperty(QueryAnalyticsConstants.ANALYTICS_SELECT_QUERY, "0");
            PropertyUtil.flushPropToPropFile(analyticsProp, QueryAnalyticsConstants.ANALYTICS_FILE_NAME);
        }
    }

    public void incrementCreateAnalayticsCount()
    {
        incrementAnalyticsProp(QueryAnalyticsConstants.ANALYTICS_CREATE_QUERY);
    }

    public void incrementUpdateAnalayticsCount()
    {
        incrementAnalyticsProp(QueryAnalyticsConstants.ANALYTICS_UPDATE_QUERY);
    }

    public void incrementDeleteAnalayticsCount()
    {
        incrementAnalyticsProp(QueryAnalyticsConstants.ANALYTICS_DELETE_QUERY);
    }

    public void incrementSelectAnalayticsCount()
    {
        incrementAnalyticsProp(QueryAnalyticsConstants.ANALYTICS_SELECT_QUERY);
    }

    public void addNewPropToAnalytics(String propName)
    {
        System.err.println("adding new prop "+propName+" to query analytics");
        Properties analyticsProp = PropertyUtil.getPropFromPropFile(QueryAnalyticsConstants.ANALYTICS_FILE_NAME);
        if(!analyticsProp.containsKey(propName))
        {
            analyticsProp.setProperty(propName, "1");
            PropertyUtil.flushPropToPropFile(analyticsProp, QueryAnalyticsConstants.ANALYTICS_FILE_NAME);
        }
    }

    private void incrementAnalyticsProp(String propName)
    {
        Properties analyticsProp = PropertyUtil.getPropFromPropFile(QueryAnalyticsConstants.ANALYTICS_FILE_NAME);
        if(analyticsProp.containsKey(propName))
        {
            System.err.println("incrementing prop "+propName+" to query analytics");
            Integer currentCount = Integer.parseInt(analyticsProp.getProperty(propName));
            currentCount++;
            analyticsProp.setProperty(propName, currentCount.toString());

            PropertyUtil.flushPropToPropFile(analyticsProp, QueryAnalyticsConstants.ANALYTICS_FILE_NAME);
        }
    }

    public String getAnalyticsData()
    {
        return FileUtil.readFileData(Path.of(QueryAnalyticsConstants.ANALYTICS_FILE_NAME));
    }
}
