package com.csci5408.distributeddatabase.reverseengineering;

import org.junit.Test;


public class ReverseEngineeringTest {

    @Test
    public void generateEntityRelationTest() throws Exception {
        ReverseEngineering reverseEngineering = new ReverseEngineering();
        reverseEngineering.reverseEngineering("demo");

        //Sample output
        //persons(name(varchar(255)),id(int),(Primary_Key: id, Foreign_Key: id)) * ----------------> N orders(id)
        //orders(name(varchar(255)),id(int),(Primary_Key: id))

    }
}