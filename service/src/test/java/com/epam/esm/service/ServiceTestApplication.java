package com.epam.esm.service;

import com.epam.esm.repository.config.DaoConfig;
import com.epam.esm.service.config.TestConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {TestConfig.class, DaoConfig.class})
public class ServiceTestApplication {

}
