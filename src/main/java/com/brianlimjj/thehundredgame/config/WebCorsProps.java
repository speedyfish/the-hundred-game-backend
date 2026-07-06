package com.brianlimjj.thehundredgame.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.web.cors")
public class WebCorsProps {

  private List<String> allowedOrigins = List.of();
}
