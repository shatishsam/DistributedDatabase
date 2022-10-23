package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.queryexecutor.constants.QueryConstants;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;

import java.io.*;
import java.util.*;

public class TableStructureHelper {

    public static ArrayList getTableStructure(String databaseName, String tableName) throws IOException {
        String line = "";
        ArrayList<LinkedHashMap<String, String>> tableData = new ArrayList<>();
        int counter = 1;
        String path= System.getProperty("user.dir")+ File.separator+databaseName+File.separator+tableName+".txt";
        File file = new File(path);
        List<String> columnsNames = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line = bufferedReader.readLine()) != null) {
            String[] rowContent = line.split("(\\*\\|){2}");
            LinkedHashMap<String, String> tableRows = new LinkedHashMap<>();
            if (counter == 1) {
                int columns = rowContent.length;
                int i = 0;
                while (i < columns) {
                    columnsNames.add(rowContent[i].trim());
                    i++;
                }
            } else {
                int i = 0;
                while (i < columnsNames.size()) {
                    tableRows.put(columnsNames.get(i).trim(), rowContent[i].trim());
                    i++;
                }
                tableData.add(tableRows);
            }
            counter++;
        }

        return tableData;
    }
}
