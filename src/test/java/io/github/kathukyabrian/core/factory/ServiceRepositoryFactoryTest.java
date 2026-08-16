package io.github.kathukyabrian.core.factory;

import org.junit.jupiter.api.Test;
import io.github.kathukyabrian.core.ServiceRepository;

class ServiceRepositoryFactoryTest {
    @Test
    void testSingletonCreated() {
        ServiceRepository serviceRepository = ServiceRepositoryFactory.getServiceRepository();
        ServiceRepository anotherServiceRepository = ServiceRepositoryFactory.getServiceRepository();

        assert serviceRepository.hashCode() == anotherServiceRepository.hashCode();
    }

}