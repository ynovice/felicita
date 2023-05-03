package com.github.ynovice.felicita;

import com.github.ynovice.felicita.config.CorsProperties;
import com.github.ynovice.felicita.config.OAuth2Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OAuth2Properties.class, CorsProperties.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
