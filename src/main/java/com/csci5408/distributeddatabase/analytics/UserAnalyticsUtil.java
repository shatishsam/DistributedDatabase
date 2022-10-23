package com.csci5408.distributeddatabase.analytics;

import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.util.PropertyUtil;

import java.nio.file.Path;
import java.util.Properties;

public class UserAnalyticsUtil
{
    public UserAnalyticsUtil()
    {
        init();
    }

    private void init()
    {
        if(!FileUtil.isFileExists(QueryAnalyticsConstants.ANALYTICS_FILE_NAME))
        {
            FileUtil.makeDirectory(UserAnalyticsConstants.ANALYTICS_DIRECTORY_NAME);
            Properties analyticsProp = new Properties();
            analyticsProp.setProperty(UserAnalyticsConstants.ANALYTICS_USER_TOTAL_QUERIES, "0");
            PropertyUtil.flushPropToPropFile(analyticsProp, UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME);
        }
    }

    public void addNewPropToAnalytics(String propName)
    {
        System.err.println("adding new prop "+propName+" to user analytics");
        Properties analyticsProp = PropertyUtil.getPropFromPropFile(UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME);
        if(!analyticsProp.containsKey(propName))
        {
            analyticsProp.setProperty(propName, "1");
            PropertyUtil.flushPropToPropFile(analyticsProp, UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME);
        }
    }

    public void incrementTotalQueriesProp()
    {
        incrementAnalyticsProp(UserAnalyticsConstants.ANALYTICS_USER_TOTAL_QUERIES);
    }

    public void incrementAnalyticsProp(String propName)
    {
        Properties analyticsProp = PropertyUtil.getPropFromPropFile(UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME);
        if(analyticsProp.containsKey(propName))
        {
            System.err.println("incrementing prop "+propName+" to user analytics");
            Integer currentCount = Integer.parseInt(analyticsProp.getProperty(propName));
            currentCount++;
            analyticsProp.setProperty(propName, currentCount.toString());

            PropertyUtil.flushPropToPropFile(analyticsProp, UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME);
        }
        else
        {
            addNewPropToAnalytics(propName);
        }
    }

    public String getAnalyticsData()
    {
        return FileUtil.readFileData(Path.of(UserAnalyticsConstants.ANALYTICS_USER_FILE_NAME));
    }
}
