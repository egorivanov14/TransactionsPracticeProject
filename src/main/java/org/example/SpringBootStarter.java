package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaRepositories("org.example.repository")
@EntityScan("org.example.entity")

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class SpringBootStarter {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Разрешаем ВСЕ пути
                        .allowedOrigins("*")     // Разрешаем ВСЕ источники
                        .allowedMethods("*")     // Разрешаем ВСЕ методы
                        .allowedHeaders("*");    // Разрешаем ВСЕ заголовки
            }
        };
    }
}

