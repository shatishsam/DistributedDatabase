package com.csci5408.distributeddatabase.util;

import java.io.*;
import java.util.Properties;

public class PropertyUtil
{
    public static boolean createPropFile(String path)
    {
        System.err.println("creating a new property file "+path);
        boolean result = true;
        try
        {
            Properties prop = new Properties();
            OutputStream output = new FileOutputStream(path);
            prop.store(output, "Metadata File");
            output.close();
        }
        catch(Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static Properties getPropFromPropFile(String propPath)
    {
        System.err.println("Reading property file from location"+propPath);
        Properties prop = new Properties();
        try
        {
            InputStream input = new FileInputStream(propPath);
            prop.load(input);
            input.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return prop;
    }

    public static boolean writeToPropFile(String propPath, String propKey, String propValue)
    {
        System.err.println("writing to an existing prop file "+propPath);
        boolean result = true;
        try
        {
            FileInputStream in = new FileInputStream(propPath);
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(propPath);
            if(!prop.containsKey(propKey))
            {
                prop.setProperty(propKey, propValue);
            }
            else if(prop.containsKey(propKey))
            {
                prop.replace(propKey, propValue);
            }
            prop.store(out, null);
            out.close();
        }
        catch(Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static boolean flushPropToPropFile(Properties prop, String path)
    {
        try
        {
            //step 1 delete the file if already exists
            //FileUtil.deleteAndCreateFileIfExists(path);
            File propFile = new File(path);
            if(propFile.exists())
            {
                propFile.delete();
            }
            propFile.createNewFile();

            //step 2 flush property to file
            OutputStream output = new FileOutputStream(path);
            prop.store(output, null);
            output.close();
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
