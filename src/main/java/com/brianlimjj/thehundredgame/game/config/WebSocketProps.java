package com.brianlimjj.thehundredgame.game.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.ws")
public class WebSocketProps {

    private List<String> allowedOrigins = List.of();

}