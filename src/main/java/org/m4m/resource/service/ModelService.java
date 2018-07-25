package org.m4m.resource.service;

import org.m4m.resource.model.DomainModel;
import org.m4m.resource.model.ResourceModel;
import org.m4m.resource.model.ServiceModel;
import org.m4m.resource.model.ServiceProvider;

import java.util.List;
import java.util.Set;

public interface ModelService {

    void registerService(DomainModel domainModel, ServiceProvider<ServiceModel> serviceProvider);

    Set<DomainModel> listSupportedServices();

    Set<DomainModel> listSupportedRestServices();

    Set<DomainModel> listOrphanSupportedServices();

    Set<ServiceProvider<ResourceModel>> listRestServiceProviders(DomainModel domainModel);

    ServiceProvider<ServiceModel> readServiceProvider(DomainModel domainModel);

    List<String> listConfiguredRoles(DomainModel domainModel);

    void registerRestService(DomainModel domainModel, ServiceProvider<ResourceModel> serviceProvider);

    void clear();

}
