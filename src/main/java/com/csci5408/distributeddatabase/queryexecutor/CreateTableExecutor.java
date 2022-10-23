package com.csci5408.distributeddatabase.queryexecutor;

import com.csci5408.distributeddatabase.distributedhelper.DistributedHelper;
import com.csci5408.distributeddatabase.localmetadatahandler.LocalMetaDataHandler;
import com.csci5408.distributeddatabase.query.CreateTableQuery;
import com.csci5408.distributeddatabase.queryexecutor.util.QueryExecutorUtil;
import com.csci5408.distributeddatabase.user.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

public class CreateTableExecutor implements IQueryExecutor{

    private static final String SEPERATOR = "*|*|";
    private String databaseName;
    private CreateTableQuery createTableQuery;
    private LocalMetaDataHandler localMetaDataHandler;

    public CreateTableExecutor(CreateTableQuery createTableQuery, String databaseName)
    {
        this.databaseName=databaseName;
        this.createTableQuery = createTableQuery;
        localMetaDataHandler = new LocalMetaDataHandler();
    }

    @Override
    public String  execute() throws IOException {
        StringBuilder result = new StringBuilder();
        String tableName = createTableQuery.getTableName();
        String primaryKey = createTableQuery.getPrimaryKey();

        DistributedHelper distributedHelper = new DistributedHelper();
        if(!distributedHelper.isDatabasePresentInLocalInstance(databaseName))
        {
            System.err.println("routing to other instance");
            result.append(distributedHelper.executeQueryInOtherInstance(this.createTableQuery.getSql()));
            return result.toString();
        }

        System.err.println("executing in current instance");
        String referenceTable="";
        String referenceTableField = "";
        String foreignKey="";
        if(null != createTableQuery.getForeignKey()){
             foreignKey = createTableQuery.getForeignKey();
        }
        if(null!=createTableQuery.getReferenceTable() && null!=createTableQuery.getReferenceTableField()){
             referenceTable = createTableQuery.getReferenceTable();
             referenceTableField = createTableQuery.getReferenceTableField();
        }
        LinkedHashMap<String,String> columns = createTableQuery.getFieldMap();

        String path= System.getProperty("user.dir")+File.separator+databaseName+File.separator+tableName+".txt";
        File file = new File(path);
        System.err.println("creting a new file for table "+ path);

        if(!QueryExecutorUtil.isTableExistsInDatabase(databaseName,tableName)){
            localMetaDataHandler.makeMetadataForTable(databaseName,tableName);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"primaryKey",primaryKey);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"foreignKey",foreignKey);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"referenceTable",referenceTable);
            localMetaDataHandler.addMetadataForTable(databaseName,tableName,"referenceTableField",referenceTableField);
            BufferedWriter writeFile = new BufferedWriter(new FileWriter(file));
            Set<String> columnNames = columns.keySet();
            for(String columnName: columnNames){
                String column= columnName;
                localMetaDataHandler.addMetadataForTable(databaseName,tableName,column,columns.get(column));
                if(columnName.equalsIgnoreCase(primaryKey)){
                    //column = column+"(PK)";
                }
                if(columnName.equalsIgnoreCase(foreignKey)){
                    //column = column + "(FK)";
                }
                //column = column + "("+ columns.get(columnName) + ")";
                writeFile.write(column+" "+SEPERATOR);
            }
            writeFile.newLine();
            writeFile.close();

            result.append("Table created successfully content");
            Logger.queryLogger("::::::::::::::::CREATE_TABLE query executed::::::::::::::::::::");
        }
        else{
            result.append("table already exists cannot create the table");
            System.out.println("File already exists");
        }
        return result.toString();
   }
}
