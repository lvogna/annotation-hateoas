package org.m4m.resource.model;

import org.m4m.Application;
import org.m4m.resource.service.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class DomainModelRegistryTest {

    private static final DomainModel DOMAIN_MODEL = new DomainModel("Resource", "action");

    @Autowired
    ModelService modelService;

    @Before
    public void setup() {
        modelService.clear();
    }

    @Test
    public void publish_one_publisher_no_subscribers() {
        // setup
        final ServiceProvider serviceProvider = new ServiceProvider(DOMAIN_MODEL);

        // perform
        modelService.registerService(DOMAIN_MODEL, serviceProvider);

        // evaluate
        final Set<DomainModel> publishedResources = modelService.listSupportedServices();
        final Set<DomainModel> subscribedResources = modelService.listSupportedRestServices();
        final Set<DomainModel> unsubscribedResources = modelService.listOrphanSupportedServices();
        assertTrue(publishedResources.contains(DOMAIN_MODEL));
        assertTrue(publishedResources.size() == 1);
        assertTrue(subscribedResources.isEmpty());
        assertTrue(unsubscribedResources.contains(DOMAIN_MODEL));
        assertTrue(unsubscribedResources.size() == 1);
    }

    @Test
    public void publish_one_publisher_one_subscriber() {
        // setup
        final ServiceProvider publisher = new ServiceProvider(DOMAIN_MODEL);
        final ServiceProvider subscriber = new ServiceProvider(DOMAIN_MODEL);

        // perform
        modelService.registerService(DOMAIN_MODEL, publisher);
        modelService.registerRestService(DOMAIN_MODEL, subscriber);

        // evaluate
        final Set<DomainModel> publishedResources = modelService.listSupportedServices();
        final Set<DomainModel> subscribedResources = modelService.listSupportedRestServices();
        final Set<DomainModel> unsubscribedResources = modelService.listOrphanSupportedServices();
        assertTrue(publishedResources.contains(DOMAIN_MODEL));
        assertTrue(publishedResources.size() == 1);
        assertTrue(subscribedResources.contains(DOMAIN_MODEL));
        assertTrue(subscribedResources.size() == 1);
        assertTrue(unsubscribedResources.isEmpty());
    }

    @Test
    public void publish_one_publisher_two_different_subscribers() {
        // setup
        final ServiceProvider publisher = new ServiceProvider(DOMAIN_MODEL);
        final ServiceProvider subscriber1 = new ServiceProvider(DOMAIN_MODEL);
        final ServiceProvider subscriber2 = new ServiceProvider(DOMAIN_MODEL);

        // perform
        modelService.registerService(DOMAIN_MODEL, publisher);
        modelService.registerRestService(DOMAIN_MODEL, subscriber1);
        modelService.registerRestService(DOMAIN_MODEL, subscriber2);

        // evaluate
        final Set<DomainModel> publishedResources = modelService.listSupportedServices();
        final Set<DomainModel> subscribedResources = modelService.listSupportedRestServices();
        final Set<DomainModel> unsubscribedResources = modelService.listOrphanSupportedServices();
        assertTrue(publishedResources.contains(DOMAIN_MODEL));
        assertTrue(publishedResources.size() == 1);
        assertTrue(subscribedResources.contains(DOMAIN_MODEL));
        assertTrue(subscribedResources.size() == 1);
        assertTrue(unsubscribedResources.isEmpty());
    }

    @Test
    public void publish_one_publisher_two_equal_subscribers() {
        // setup
        final ServiceProvider publisher = new ServiceProvider(DOMAIN_MODEL);
        final ServiceProvider subscriber1 = new ServiceProvider(DOMAIN_MODEL);
        final ServiceProvider subscriber2 = subscriber1;

        // perform
        modelService.registerService(DOMAIN_MODEL, publisher);
        modelService.registerRestService(DOMAIN_MODEL, subscriber1);
        modelService.registerRestService(DOMAIN_MODEL, subscriber2);

        // evaluate
        final Set<DomainModel> publishedResources = modelService.listSupportedServices();
        final Set<DomainModel> subscribedResources = modelService.listSupportedRestServices();
        final Set<DomainModel> unsubscribedResources = modelService.listOrphanSupportedServices();
        assertTrue(publishedResources.contains(DOMAIN_MODEL));
        assertTrue(publishedResources.size() == 1);
        assertTrue(subscribedResources.contains(DOMAIN_MODEL));
        assertTrue(subscribedResources.size() == 1);
        assertTrue(unsubscribedResources.isEmpty());
    }


}