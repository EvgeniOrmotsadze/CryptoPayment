package com.radlane.payment.configuration;


import com.radlane.payment.interceptor.IpVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Value("${crypto.allowed.ip}")
    private String allowedIp;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpVerifier(allowedIp))
                .addPathPatterns("/api/channels/callback");
    }
}