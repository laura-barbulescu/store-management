package com.productmanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@EnableJpaRepositories(basePackages = "com.productmanagement.persistence.repository")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class Config {
}
