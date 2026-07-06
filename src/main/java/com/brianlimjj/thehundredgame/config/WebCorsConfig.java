// src/main/java/com/brianlimjj/thehundredgame/config/WebCorsConfig.java
package com.brianlimjj.thehundredgame.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(WebCorsProps.class)
public class WebCorsConfig implements WebMvcConfigurer {

  private final WebCorsProps webCorsProps;

  public WebCorsConfig(WebCorsProps webCorsProps) {
    this.webCorsProps = webCorsProps;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(webCorsProps.getAllowedOrigins().toArray(String[]::new))
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
