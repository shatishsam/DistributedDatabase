package com.csci5408.distributeddatabase.util;

import java.io.File;
import java.util.*;

public class ReadMetaDataUtil {

    public static Map<String, String> getMetadata(File file) throws Exception {

        Properties fileProperties = PropertyUtil.getPropFromPropFile(file.getPath());
        Set tableSet = fileProperties.entrySet();

        String primaryKey = fileProperties.getProperty("primaryKey");
        String foreignKey = fileProperties.getProperty("foreignKey");
        String referenceTable = fileProperties.getProperty("referenceTable");
        String referenceTableField = fileProperties.getProperty("referenceTableField");
        String tableColumn = "";

        Iterator iterator = tableSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry tableMap = (Map.Entry) iterator.next();
            String key = (String) tableMap.getKey();
            String value = (String) tableMap.getValue();

            if (! (key.equals("primaryKey") || key.equals("foreignKey") || key.equals("referenceTable")
                    || key.equals("referenceTableField"))) {
                tableColumn += key + " " + value + ", ";
            }
        }

        Map<String, String> tableContent = new HashMap<>();
        tableContent.put("primaryKey", primaryKey);
        tableContent.put("foreignKey", foreignKey);
        tableContent.put("referenceTable", referenceTable);
        tableContent.put("referenceTableField", referenceTableField);
        tableContent.put("tableColumn", tableColumn);

        return tableContent;
    }
}
