package com.csci5408.distributeddatabase.reverseengineering;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.user.Logger;
import com.csci5408.distributeddatabase.util.FileUtil;
import com.csci5408.distributeddatabase.util.ReadMetaDataUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class ReverseEngineering {

    public String reverseEngineering(String databaseName) throws Exception {

        DistributedHelper distributedHelper = new DistributedHelper();
        if(!distributedHelper.isDatabasePresentInLocalInstance(databaseName))
        {
            return distributedHelper.executeReverseEngineeringInOtherInstance(databaseName);
        }

        String directoryPath = "LOCALMETADATA/" + databaseName;
        boolean directoryExists = FileUtil.createDirectory("ER");
        File directory = new File(directoryPath);
        String outputPath = "ER/" + databaseName +"_Entity_Relationship.txt";

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath));
        if(directory.exists()) {
            for(File file: directory.listFiles()) {

                String tableName = file.getName().replace(".properties","");
                String singleTableEntityRelation = tableName + "(";

                Map<String, String> tableContent = ReadMetaDataUtil.getMetadata(file);

                String primaryKey = null;
                String foreignKey = null;
                String referenceTable = null;
                String referenceTableField = null;

                if(!tableContent.isEmpty()) {
                    primaryKey = tableContent.get("primaryKey");
                    foreignKey = tableContent.get("foreignKey");
                    referenceTable = tableContent.get("referenceTable");
                    referenceTableField = tableContent.get("referenceTableField");
                    String tableColumn = tableContent.get("tableColumn");

                    if (tableColumn != null) {
                        String[] tableColumns = tableColumn.split(", ");
                        for (String singleColumn : tableColumns) {
                            String[] columnAndDataType = singleColumn.split(" ");
                            singleTableEntityRelation += columnAndDataType[0] + "(" + columnAndDataType[1] + "),";
                        }
                    }
                }

                String cardinality = "";
                File tableData = new File(databaseName+"/"+tableName+".txt");
                if(!foreignKey.isEmpty() && tableData.exists()) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(tableData));
                    String line = "";
                    int fkPosition = 0;
                    boolean fkPositionFound = false;
                    Set<String> fkSet = new HashSet<>();

                    boolean firstLine = true;
                    while((line = bufferedReader.readLine()) != null) {
                        if(firstLine) {
                            String[] coulmnNames = line.split("\\*\\|\\*\\|");
                            for(String columnName: coulmnNames) {
                                String column = (columnName.equals(foreignKey)) ? columnName : columnName.substring(0, columnName.length()-1);
                                if(column.equals(foreignKey)) {
                                    fkPositionFound = true;
                                } else if (!(column.equals(foreignKey)) && !fkPositionFound) {
                                    fkPosition++;
                                }
                            }
                            firstLine = false;
                        }
                        else {
                            String[] columnData = line.split("\\*\\|\\*\\|");
                            if(!fkSet.isEmpty() && fkSet.contains(columnData[fkPosition])) {
                                cardinality = "N";
                                break;
                            } else {
                                fkSet.add(columnData[fkPosition]);
                            }
                        }
                    }
                    cardinality = cardinality == "" ? "1" : cardinality;
                }

                singleTableEntityRelation += getRelationString(primaryKey, foreignKey, referenceTable, referenceTableField, cardinality);

                bufferedWriter.write(singleTableEntityRelation);
                bufferedWriter.newLine();

            }
        } else {
            Logger.generalLogger("* Couldn't Generate Entity Relationship *");
            throw new Exception("Database doesn't exist");
        }
        bufferedWriter.close();
        Logger.generalLogger("::::::::::::::::Entity Relationship Generated::::::::::::::::::::");
        return FileUtil.readFileData(Path.of(outputPath));
    }

    private String getRelationString(String primaryKey, String foreignKey, String foreignKeyTableName, String primaryKeyOfForeignKeyTable, String cardinality) {
        String relationString = "";
        String relationWithCardinality = " * ----------------> " + cardinality + " ";

        if(!primaryKey.isEmpty()) {
            relationString += "(Primary_Key: " + primaryKey;
        }

        if(!(foreignKey.isEmpty() && foreignKeyTableName.isEmpty() && primaryKeyOfForeignKeyTable.isEmpty())) {
            relationString += ", Foreign_Key: " + foreignKey + "))" + relationWithCardinality + foreignKeyTableName + "(" + primaryKeyOfForeignKeyTable + ")";
        } else {
            relationString += "))";
        }

        return relationString;
    }
}
