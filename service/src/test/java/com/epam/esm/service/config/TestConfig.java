package com.epam.esm.service.config;

import com.epam.esm.service.dto.converter.impl.CertificateDtoConverter;
import com.epam.esm.service.dto.converter.impl.OrderDtoConverter;
import com.epam.esm.service.dto.converter.impl.TagDtoConverter;
import com.epam.esm.service.dto.converter.impl.UserDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class TestConfig {

    @Bean
    public ConversionService getConversionService() {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();

        Set<GenericConverter> converters = new HashSet<>();
        converters.add(new UserDtoConverter());
        converters.add(new TagDtoConverter());
        converters.add(new CertificateDtoConverter());
        converters.add(new OrderDtoConverter());

        factoryBean.setConverters(converters);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
