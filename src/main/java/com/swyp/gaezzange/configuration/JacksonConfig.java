package com.swyp.gaezzange.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  private static final String dateFormat = "yyyy-MM-dd";
  private static final String timeFormat = "kk:mm:ss";
  private static final String datetimeFormat = "yyyy-MM-ddTkk:mm:ss";


  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    JavaTimeModule jtm = new JavaTimeModule();
    jtm.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
    jtm.addDeserializer(LocalTime.class,
        new LocalTimeDeserializer(DateTimeFormatter.ISO_TIME));
    jtm.addDeserializer(LocalDate.class,
        new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));

    return builder -> {
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      builder.modules(jtm);
    };
  }
}
