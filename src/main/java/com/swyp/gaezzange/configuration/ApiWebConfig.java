package com.swyp.gaezzange.configuration;

import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiWebConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("/api", c ->
        c.isAnnotationPresent(RestController.class)
            && c.getPackage().getName().startsWith("com.swyp.gaezzange.api"));
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
    registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
    registrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME);
    registrar.registerFormatters(registry);
  }
}
