package com.github.ynovice.felicita.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.img")
@Getter
@Setter
public class ImgProperties {

    private String uploadDirectory;
}
