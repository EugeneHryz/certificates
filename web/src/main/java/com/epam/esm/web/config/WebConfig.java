package com.epam.esm.web.config;

import com.epam.esm.service.dto.converter.impl.CertificateDtoConverter;
import com.epam.esm.service.dto.converter.impl.OrderDtoConverter;
import com.epam.esm.service.dto.converter.impl.TagDtoConverter;
import com.epam.esm.service.dto.converter.impl.UserDtoConverter;
import com.epam.esm.web.model.converter.impl.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@EnableTransactionManagement
@ComponentScan({"com.epam.esm.web"})
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.configure(new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false));
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().modules(new JavaTimeModule()).build()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new TagModelConverter());
//        registry.addConverter(new UserModelConverter());
//        registry.addConverter(new OrderModelConverter());
//        registry.addConverter(new CertificateModelConverter());
//
//        registry.addConverter(new TagDtoConverter());
//        registry.addConverter(new UserDtoConverter());
//        registry.addConverter(new OrderDtoConverter());
//        registry.addConverter(new CertificateDtoConverter());
//    }
}
