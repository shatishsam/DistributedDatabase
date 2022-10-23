package com.csci5408.distributeddatabase.dataexport;

import org.junit.Test;


public class DataExportTest {

    @Test()
    public void generateSQLDumpTest() throws Exception {
        DataExport dataExport = new DataExport();
        dataExport.exportSQLDump("demo");

        //Sample output
        /*
        CREATE DATABASE IF NOT EXISTS demo;
        USE demo;

        *****************************
        DROP TABLE IF EXISTS persons;
        CREATE TABLE persons(name varchar(255), id int, primary key (id) , foreign key (id) references orders(id));

        *****************************
        INSERT INTO persons(id , name , lastname ) VALUES (1, janvi, patel),(2, shathish, annamalai),(1, janvi, patel),(2, shathish, annamalai);

        *****************************
         */
    }
}
