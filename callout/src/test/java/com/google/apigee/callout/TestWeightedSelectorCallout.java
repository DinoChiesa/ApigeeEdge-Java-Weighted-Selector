package com.google.apigee.callout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import mockit.Mock;
import mockit.MockUp;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.message.MessageContext;
import com.apigee.flow.message.Message;

import com.google.apigee.callout.WeightedSelectorCallout;

public class TestWeightedSelectorCallout {

    MessageContext msgCtxt;
    String messageContent;
    Message message;
    ExecutionContext exeCtxt;

    @BeforeMethod()
    public void testSetup1() {

        msgCtxt = new MockUp<MessageContext>() {
            private Map variables;
            public void $init() {
                variables = new HashMap();
            }

            @Mock()
            public <T> T getVariable(final String name){
                if (variables == null) {
                    variables = new HashMap();
                }
                return (T) variables.get(name);
            }

            @Mock()
            public boolean setVariable(final String name, final Object value) {
                if (variables == null) {
                    variables = new HashMap();
                }
                variables.put(name, value);
                return true;
            }

            @Mock()
            public boolean removeVariable(final String name) {
                if (variables == null) {
                    variables = new HashMap();
                }
                if (variables.containsKey(name)) {
                    variables.remove(name);
                }
                return true;
            }

            @Mock()
            public Message getMessage() {
                return message;
            }
        }.getMockInstance();

        exeCtxt = new MockUp<ExecutionContext>(){ }.getMockInstance();

        message = new MockUp<Message>(){
            @Mock()
            public InputStream getContentAsStream() {
                return new ByteArrayInputStream(messageContent.getBytes(StandardCharsets.UTF_8));
            }
        }.getMockInstance();
    }


    @Test
    public void test_BasicConfig() {
        Map properties = new HashMap();
        properties.put("weights", "1,2,3,4,5");
        properties.put("debug", "true");
        WeightedSelectorCallout callout = new WeightedSelectorCallout(properties);  // properties

        // execute and retrieve output
        ExecutionResult actualResult = callout.execute(msgCtxt, exeCtxt);
        ExecutionResult expectedResult = ExecutionResult.SUCCESS ;
        Assert.assertEquals(actualResult, expectedResult, "result not as expected");

        String bucket = msgCtxt.getVariable("wrs_bucket");
        Integer value = Integer.valueOf(bucket);
        Assert.assertTrue(value > -1 && value < 99, "result out of range");
        System.out.println("=========================================================");
    }

}
