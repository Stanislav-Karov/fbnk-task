package org.example.cashdesk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Autowired
    private ApiKeyAuthFilter apiKeyAuthFilter;

    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyAuthFilterRegistrationBean() {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyAuthFilter);
        registrationBean.addUrlPatterns("/api/v1/*"); // Apply filter to specific URL patterns
        return registrationBean;
    }
}
