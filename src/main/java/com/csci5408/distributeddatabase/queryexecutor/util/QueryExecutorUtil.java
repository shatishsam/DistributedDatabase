package com.csci5408.distributeddatabase.queryexecutor.util;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.query.Criteria;
import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class QueryExecutorUtil
{

    public static boolean isDataBaseExists(String databaseName)
    {
        GlobalMetadataHandler metadataHandler = new GlobalMetadataHandler();
        Properties globalProps = metadataHandler.getGlobalMetadataProperties();
        System.out.println("checking if "+databaseName+ " exists result = " +globalProps.containsKey(databaseName));
        return globalProps.containsKey(databaseName);
    }

    public static boolean isDatabaseExistsInLocal(String databaseName)
    {
        return FileUtil.isDirectoryExists(databaseName);
    }

    public static boolean isTableExistsInDatabase(String databaseName, String tableName)
    {
        String filePath = databaseName+ File.separator+tableName+".txt";
        return FileUtil.isFileExists(filePath);
    }

    public static boolean isDatabaseChosen()
    {
        Properties prop = System.getProperties();
        if(prop.getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE)==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static String getChosenDatabase() throws Exception
    {
        String currentDBName = new String();
        if(!isDatabaseChosen())
        {
            System.err.println(QueryConstants.NO_DATABASE_CHOSEN_ERROR);
            throw new Exception(QueryConstants.NO_DATABASE_CHOSEN_ERROR);
        }
        else
        {
            currentDBName=System.getProperty(QueryConstants.PROPERTY_CURRENT_DATABASE);
        }
        return currentDBName;
    }

    public static String getTableFileName(String databaseName, String tableName)
    {
        String filePath = databaseName+File.separator+tableName+".txt";
        return filePath;
    }

    public static boolean isColumnInteger(String value)
    {
        try
        {
            Integer.parseInt(value);
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }

    public static boolean checkCriteriaForRow(HashMap<String, String> row, Criteria criteria)
    {
        try
        {
            Set<String> columns = row.keySet();
            for(String columnName : columns)
            {
                String columnValue = row.get(columnName);
                columnName = columnName.trim();
                if(columnName.equals(criteria.getLeftOperand()))
                {
                    boolean isColumnInteger = isColumnInteger(columnValue);
                    if(criteria.getOperator().equals(">"))
                    {
                        int comparingValue = Integer.parseInt(criteria.getRightOperand());
                        int rowValue = Integer.parseInt(columnValue);
                        return rowValue > comparingValue;
                    }
                    else if(criteria.getOperator().equals("<"))
                    {
                        int comparingValue = Integer.parseInt(criteria.getRightOperand());
                        int rowValue = Integer.parseInt(columnValue);
                        return rowValue < comparingValue;
                    }
                    else if(criteria.getOperator().equals("="))
                    {
                        if(isColumnInteger)
                        {
                            int comparingValue = Integer.parseInt(criteria.getRightOperand());
                            int rowValue = Integer.parseInt(columnValue);
                            return rowValue == comparingValue;
                        }
                        else
                        {
                            return columnValue.equals(criteria.getRightOperand());
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
