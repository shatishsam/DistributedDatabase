package com.csci5408.distributeddatabase.globalmetadatahandler;

import java.io.*;
import java.util.Properties;

public class GlobalMetadataHandler
{
    public GlobalMetadataHandler()
    {
        initGlobalMetaData();
    }

    public boolean initGlobalMetaData()
    {
        boolean result = false;
        try
        {
            File propFile = new File(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH);

            if(!propFile.exists())
            {
                System.err.println("creating new global metadata file" +propFile.createNewFile());
            }

            FileInputStream in = new FileInputStream(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH);
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH);
            if(!prop.containsKey("current_instance"))
            {
                prop.setProperty("current_instance", GlobalMetadataConstants.INSTANCE_CURRENT);
            }
            prop.store(out, null);
            out.close();

            result=true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean writeToMetaData(String propName, String propValue)
    {
        boolean result = false;
        try
        {
            OutputStream output = new FileOutputStream(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH, true);
            Properties  prop = getGlobalMetadataProperties();

            if(!prop.containsKey(propName))
            {
                prop.setProperty(propName, propValue);
            }
            else if(prop.containsKey(propName))
            {
                prop.replace(propName, propValue);
            }

            PrintWriter writer = new PrintWriter(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH);
            writer.print("");
            writer.close();

            prop.store(output, null);
            result=true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public Properties getGlobalMetadataProperties()
    {
        Properties prop = new Properties();
        try
        {
            InputStream input = new FileInputStream(GlobalMetadataConstants.GLOBAL_METADATA_FILE_PATH);
            prop.load(input);
            input.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return prop;
    }
}
