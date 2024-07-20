package com.swyp.gaezzange.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private String desc = "개짱이 프로젝트 API <br>"
        + "기본 날짜/시간 요청, 응답 형식입니다. - 스웨거 스펙에 반영 안됨 <br>"
        + "<br>"
        + "time: \"00:00:01\", <br>"
        + "date: \"2024-11-11\", <br>"
        + "dateTime: \"2024-11-11T01:01:01\" <br>";


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("개짱이 프로젝트")
                .description(desc)
                .version("1.0.0");
    }
}