package com.epam.esm.service;

import com.epam.esm.repository.config.PersistenceConfig;
import com.epam.esm.service.config.TestConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {TestConfig.class, PersistenceConfig.class})
public class ServiceTestApplication {

}
