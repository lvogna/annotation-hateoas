package org.m4m.resource.service;

import org.m4m.Application;
import org.m4m.service.StringService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class DomainStringServiceTest {

    private final static String INPUT_STRING="test";
    private final static String EXPECTED_OUTPUT_STRING="TEST";

    @Autowired
    private StringService stringService;

    @Test
    public void toUpper() {
        String output = stringService.toUpperCase(INPUT_STRING);
        Assert.assertEquals(EXPECTED_OUTPUT_STRING, output);
    }


}