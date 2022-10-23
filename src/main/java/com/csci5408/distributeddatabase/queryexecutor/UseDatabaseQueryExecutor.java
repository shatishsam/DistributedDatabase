package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.query.UseDatabaseQuery;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;
import com.csci5408.distributeddatabase.user.Logger;

public class UseDatabaseQueryExecutor implements IQueryExecutor
{
    private UseDatabaseQuery useDatabaseQuery;
    private boolean isPresentLocal = false;
    private boolean isPresentOther = false;

    public UseDatabaseQueryExecutor(UseDatabaseQuery useDatabaseQuery)
    {
        this.useDatabaseQuery=useDatabaseQuery;
    }

    @Override
    public String execute() throws Exception
    {
        DistributedHelper distributedHelper = new DistributedHelper();
        isPresentLocal = distributedHelper.isDatabasePresentInLocalInstance(useDatabaseQuery.getDatabaseName());
        System.err.println("Present in Local:::::::::"+isPresentLocal);
        System.err.println("Present in Other:::::::::"+isPresentOther);
        isPresentOther = distributedHelper.isDatabasePresentInOtherInstance(useDatabaseQuery.getDatabaseName());
        if(isPresentLocal || isPresentOther){
            System.setProperty(QueryConstants.PROPERTY_CURRENT_DATABASE, useDatabaseQuery.getDatabaseName());
            System.out.println(System.getProperties());

            distributedHelper.updateSystemProperties(QueryConstants.PROPERTY_CURRENT_DATABASE,  useDatabaseQuery.getDatabaseName());

            Logger.queryLogger("::::::::::::::::USE_DATABASE query executed::::::::::::::::::::");
            return System.getProperties().getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE);
        }
        else
            return "Database not found";

    }
}
