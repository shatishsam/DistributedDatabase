package com.csci5408.distributeddatabase.util;

import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class FileUtil
{
    public static boolean isFileExists(String path)
    {
        System.err.println("checking if file exists "+path);
        return new File(path).exists();
    }

    public static boolean isDirectoryExists(String path)
    {
        System.err.println("checking if directory exists "+path);
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }

    public static boolean createDirectory(String path) {
        return new File(path).mkdirs();
    }

    public static String readFileData(Path path) {
        String fileContent = "";
        try {
            fileContent = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static boolean makeDirectory(String path)
    {
        System.err.println("Making a new Directory"+path);
        boolean result = true;
        try
        {
            File directory = new File(path);
            if (! directory.exists())
            {
                directory.mkdir();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            result=false;
        }
        return result;
    }

    public static boolean writeToExistingFile(String path, String line) throws Exception
    {
        if(!FileUtil.isFileExists(path))
        {
            System.err.println(path+ "No such file exists");
            throw new FileNotFoundException();
        }

        FileWriter fileWriter = new FileWriter(path, true);

        fileWriter.append(line);
        fileWriter.append('\n');

        fileWriter.flush();
        fileWriter.close();

        return true;
    }

    public static void deleteAndCreateFileIfExists(String filePath) throws Exception
    {
        if(new File(filePath).exists())
        {
            System.err.println(filePath+" already exists deleting and creating it again");
            PrintWriter writer = new PrintWriter(filePath);
            writer.write("");
            writer.close();
        }
    }

    public static boolean writeTableHashMapToFile(ArrayList<LinkedHashMap<String, String>> tableContent, String tablePath)
    {
        boolean result = true, isHeaderRowCreated=false;
        try
        {
            //step 1 delete file if already exists
            deleteAndCreateFileIfExists(tablePath);

            //write the content to file
            for(HashMap<String, String> row : tableContent)
            {
                String line = new String();
                Set<String> columns = row.keySet();

                //trim any empty spaces present in the column names
                columns.forEach(item->{
                    item = item.trim();
                });
                System.err.println("Columns are trimming the space is"+columns);

                //create header row for the table if not done before
                if(!isHeaderRowCreated)
                {
                    line += String.join(QueryConstants.SEPARATOR_ROW_COLUMN, columns);
                    line += "\n";
                    isHeaderRowCreated=true;
                }

                //write all the rows to the table
                for(String key : columns)
                {
                    line += row.get(key)+QueryConstants.SEPARATOR_ROW_COLUMN;
                }
                FileUtil.writeToExistingFile(tablePath, line);
            }
        }
        catch (Exception ex)
        {
            result=false;
            ex.printStackTrace();
        }

        return result;
    }
}
