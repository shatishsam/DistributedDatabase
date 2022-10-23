package com.csci5408.distributeddatabase.controller;

import com.csci5408.distributeddatabase.dataexport.DataExport;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import com.csci5408.distributeddatabase.queryexecutor.QueryExecutor;
import com.csci5408.distributeddatabase.reverseengineering.ReverseEngineering;
import com.csci5408.distributeddatabase.user.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

@RestController
public class DatabaseController
{

    @GetMapping("/getGlobalMetaDataProp")
    public Properties getGlobalMetaData() throws IOException {
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        Logger.eventLogger("Getting global meta data property");
        return globalMetadataHandler.getGlobalMetadataProperties();
    }

    @PostMapping("/updateGlobalMetaDataProp")
    public Properties redirectUpdateGlobalMetadataProp(@RequestParam  String propName, @RequestParam  String propValue) throws IOException {
        System.err.println("update global properties = "+propName+" _ "+propValue);
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        globalMetadataHandler.writeToMetaData(propName, propValue);
        Logger.eventLogger("Updating global meta data properties for both the instances");
        return globalMetadataHandler.getGlobalMetadataProperties();
    }

    @PostMapping("/executeQuery")
    public String executeQuery(@RequestParam String query) throws IOException {
        System.err.println("executing query "+ query);
        QueryExecutor queryExecutor = new QueryExecutor(query);
        Logger.eventLogger("Executing query");
        return queryExecutor.executeQuery();
    }

    @PostMapping("/executeTransaction")
    public String executeTransaction(@RequestParam String transactionQuery) throws Exception
    {
        System.err.println("executing transaction for other instance"+ transactionQuery);
        QueryExecutor queryExecutor = new QueryExecutor(transactionQuery);
        Logger.eventLogger("Executing Transaction");
        return queryExecutor.executeTransaction(transactionQuery);
    }

    @PostMapping("/exportDump")
    public String exportSQLDump(@RequestParam String databaseName) throws Exception
    {
        DataExport export = new DataExport();
        String result = export.exportSQLDump(databaseName);
        Logger.eventLogger("Exporting the Dump");
        return result;
    }

    @PostMapping("/reverseEngineering")
    public String performReverseEngineering(@RequestParam String databaseName) throws Exception
    {
        //ToDo changes for reverse engineering
        ReverseEngineering reverseEngineering = new ReverseEngineering();
        String result = reverseEngineering.reverseEngineering(databaseName);
        Logger.eventLogger("Reverse Engineering performed");
        return result;
    }

    @PostMapping("/updateSystemProperties")
    public String updateSystemProperties(@RequestParam String propName, @RequestParam String propValue) throws Exception
    {
        System.setProperty(propName, propValue);
        System.err.println(System.getProperties());
        Logger.eventLogger("Updating the System Properties");
        return "updated succesfuly";
    }

    //test controller
    @PostMapping("/redirectUpdateGlobalMetaDataProp")
    public String updateGlobalMetadataProp(@RequestParam  String propName, @RequestParam  String propValue) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://35.226.94.226:8080/redirectUpdateGlobalMetaDataProp";

        System.err.println("routing to url "+url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("propName", propName);
        map.add("propValue", propValue);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        Logger.eventLogger("Redirecting to update the global meta data property");

        return response.getBody();
    }
}
