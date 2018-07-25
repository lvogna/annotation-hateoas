package org.m4m.resource.collector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.m4m.Application;
import org.m4m.resource.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class DomainModelCollectorTest {

    private final static Logger logger = LoggerFactory.getLogger(DomainModelCollectorTest.class);


    @Autowired
    private ModelCollector modelCollector;

    @Autowired
    private ModelService modelService;

    @Test
    public void publishResources() throws Throwable {
        modelCollector.collect();
        modelService.listSupportedServices().forEach(domainModel -> logger.info("supported domain model: {}", domainModel));
    }
}