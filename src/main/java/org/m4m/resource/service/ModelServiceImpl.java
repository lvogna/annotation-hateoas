package org.m4m.resource.service;

import org.m4m.resource.model.DomainModel;
import org.m4m.resource.model.ResourceModel;
import org.m4m.resource.model.ServiceModel;
import org.m4m.resource.model.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModelServiceImpl implements ModelService {

    final static Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);


    private final Map<DomainModel, Set<ServiceProvider<ResourceModel>>> restServices = new HashMap<>();
    private final Map<DomainModel, ServiceProvider<ServiceModel>> services = new HashMap<>();


    @Override
    public void registerService(DomainModel domainModel, ServiceProvider<ServiceModel> serviceProvider) {
        assert domainModel != null : "domainModel cannot be null";
        assert serviceProvider != null : "serviceProvider cannot be null";
        if (this.services.containsKey(domainModel)) {
            logger.warn("skipping domainModel [{}] already defined in [{}]", domainModel, this.services.keySet());
        } else {
            this.services.put(domainModel, serviceProvider);
        }
    }

    @Override
    public Set<DomainModel> listSupportedServices() {
        return Collections.unmodifiableSet(this.services.keySet());
    }

    @Override
    public Set<DomainModel> listSupportedRestServices() {
        return Collections.unmodifiableSet(this.restServices.keySet());
    }

    @Override
    public Set<ServiceProvider<ResourceModel>> listRestServiceProviders(DomainModel domainModel) {
        assert domainModel != null : "domainModel cannot be null";
        return this.restServices.get(domainModel);
    }

    @Override
    public ServiceProvider<ServiceModel> readServiceProvider(DomainModel domainModel) {
        assert domainModel != null : "domainModel cannot be null";
        return this.services.get(domainModel);
    }

    @Override
    public Set<DomainModel> listOrphanSupportedServices() {
        return this.services.keySet().
                stream().
                filter(resourceModel -> !this.restServices.keySet().contains(resourceModel)).
                collect(Collectors.toSet());
    }

    @Override
    public List<String> listConfiguredRoles(DomainModel domainModel) {
        assert domainModel != null : "domainModel cannot be null";
        final ServiceProvider<ServiceModel> serviceResourceModelServiceProvider = services.get(domainModel);
        final Secured annotation = serviceResourceModelServiceProvider.getModel().getActualMethod().getAnnotation(Secured.class);
        return annotation == null ? Collections.emptyList() : Arrays.asList(annotation.value());
    }


    @Override
    public void registerRestService(DomainModel domainModel, ServiceProvider<ResourceModel> serviceProvider) {
        assert domainModel != null : "domainModel cannot be null";
        assert serviceProvider != null : "serviceProvider cannot be null";
        final Set<ServiceProvider<ResourceModel>> restServices;
        if (this.restServices.containsKey(domainModel)) {
            restServices = this.restServices.get(domainModel);
            restServices.add(serviceProvider);
        } else {
            restServices = new HashSet<>();
            restServices.add(serviceProvider);
            this.restServices.put(domainModel, restServices);
        }

    }

    @Override
    public void clear() {
        this.services.clear();
        this.restServices.clear();
    }

}
