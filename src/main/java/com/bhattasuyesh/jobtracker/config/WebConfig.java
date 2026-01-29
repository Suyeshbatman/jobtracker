package com.bhattasuyesh.jobtracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Forward routes like /dashboard, /login, /register (no dot)
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");

        // Forward nested routes like /dashboard/settings (no dot)
        registry.addViewController("/**/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}
