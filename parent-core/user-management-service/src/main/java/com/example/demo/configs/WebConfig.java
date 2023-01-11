package com.example.demo.configs;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${ratelimit.bucketCapacity}")
  private Integer bucketCapacity;

  @Value("${ratelimit.durationInMinutes}")
  private Integer durationInMinutes;

  @Bean
  @Scope("prototype")
  public Bandwidth bandwidth() {
    return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofMinutes(durationInMinutes)));
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
