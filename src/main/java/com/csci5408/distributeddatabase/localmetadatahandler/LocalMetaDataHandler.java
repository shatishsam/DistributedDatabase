package com.csci5408.distributeddatabase.localmetadatahandler;

import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.util.PropertyUtil;

import java.io.File;
import java.util.Properties;

public class LocalMetaDataHandler
{
    public LocalMetaDataHandler()
    {
        //ToDo init local metadata folder structure
        FileUtil.makeDirectory(LocalMetaDataConstants.LOCAL_METADATA_PATH);
    }

    public void makeMetadataForDatabase(String databaseName)
    {
        String dbMetaDataPath = getDatabaseMetadataFolderPath(databaseName);
        System.err.println("creating new metadata for database "+ dbMetaDataPath);
        FileUtil.makeDirectory(dbMetaDataPath);
    }

    public void makeMetadataForTable(String databaseName, String tableName)
    {
        System.err.println("creating new metadata for table db="+ databaseName+" table="+tableName);
        String tableMetadataPath = getTableMetadataFilePath(databaseName, tableName);
        System.err.println("creating table metadata prop= "+tableMetadataPath);
        PropertyUtil.createPropFile(tableMetadataPath);
    }

    public Properties getTableMetadataProp(String databaseName, String tableName)
    {
        String tablePropPath = getTableMetadataFilePath(databaseName, tableName);
        System.err.println("extracting prop file from path= "+ tablePropPath);
        return PropertyUtil.getPropFromPropFile(tablePropPath);
    }

    public void addMetadataForTable(String databaseName, String tableName, String propKey, String propValue)
    {
        System.err.println("adding table metadata info table="+ databaseName+" table="+tableName+ " prop name = "+propKey+" prop value= "+propValue);
        String tablePropPath = getTableMetadataFilePath(databaseName, tableName);
        PropertyUtil.writeToPropFile(tablePropPath, propKey, propValue);
    }

    public static String getDatabaseMetadataFolderPath(String databaseName)
    {
        return LocalMetaDataConstants.LOCAL_METADATA_PATH + File.separator +databaseName;
    }

    public String getTableMetadataFilePath(String databaseName, String tableName)
    {
        String path = getDatabaseMetadataFolderPath(databaseName)+ File.separator +tableName + ".properties";
        return path;
    }
}
