package com.csci5408.distributeddatabase.distributedhelper;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataConstants;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class DistributedHelper
{
    public boolean isDatabasePresentInLocalInstance(String databaseName)
    {
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        Properties prop = globalMetadataHandler.getGlobalMetadataProperties();
        String currentInstanceIP = prop.getProperty("current_instance");

        if(prop.containsKey(databaseName))
        {
            String databaseInstanceIp = prop.getProperty(databaseName);
            System.err.println("current instance IP="+currentInstanceIP+" database ip= "+databaseInstanceIp);
            System.err.println("Is database present in current instance = "+ currentInstanceIP.equals(databaseInstanceIp));
            return currentInstanceIP.equals(databaseInstanceIp);
        }
        else
        {
            return false;
        }
    }

    public boolean isDatabasePresentInOtherInstance(String databaseName)
    {
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        Properties prop = globalMetadataHandler.getGlobalMetadataProperties();
        System.err.println("global metadata prop is "+prop);

        if(prop.containsKey(databaseName))
        {
            String databaseInstanceIp = prop.getProperty(databaseName);
            System.err.println("Other instance IP="+GlobalMetadataConstants.INSTANCE_OTHER+" database ip= "+databaseInstanceIp);
            System.err.println("Is database present in current instance = "+ GlobalMetadataConstants.INSTANCE_OTHER.equals(databaseInstanceIp));
            return GlobalMetadataConstants.INSTANCE_OTHER.equals(databaseInstanceIp);
        }
        else
        {
            return false;
        }
    }


    public String updateGlobalMetadataPropInOtherInstance(String keyName, String keyValue)
    {
        String mapping = "updateGlobalMetaDataProp";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("propName", keyName);
        parameterMap.add("propValue", keyValue);
        return forwardRequestToOtherInstance(mapping, parameterMap).toString();
    }

    public String executeQueryInOtherInstance(String query)
    {
        String mapping = "executeQuery";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("query", query);
        return forwardRequestToOtherInstance(mapping, parameterMap).getBody();
    }

    public String executeTransactionInOtherInstance(String transactionQuery)
    {
        String mapping = "executeTransaction";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("transactionQuery", transactionQuery);
        return forwardRequestToOtherInstance(mapping, parameterMap).getBody();
    }

    public String executeSQLDumpInOtherInstance(String databaseName)
    {
        String mapping = "exportDump";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("databaseName", databaseName);
        return forwardRequestToOtherInstance(mapping, parameterMap).getBody();
    }

    public String executeReverseEngineeringInOtherInstance(String databaseName)
    {
        String mapping="reverseEngineering";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("databaseName", databaseName);
        return forwardRequestToOtherInstance(mapping, parameterMap).getBody();
    }


    public String updateSystemProperties(String propName, String propValue)
    {
        String mapping="updateSystemProperties";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("propName", propName);
        parameterMap.add("propValue", propValue);
        return forwardRequestToOtherInstance(mapping, parameterMap).getBody();
    }

    private ResponseEntity<String> forwardRequestToOtherInstance(String mapping, MultiValueMap<String, String> parameterMap)
    {
        String url = "http://"+ GlobalMetadataConstants.INSTANCE_OTHER+"/"+mapping;
        System.err.println("routing query url "+url);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameterMap, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        return response;
    }
}
