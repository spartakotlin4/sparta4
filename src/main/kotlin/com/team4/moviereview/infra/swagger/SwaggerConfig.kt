package com.team4.moviereview.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(
            Info()
                .title("이름 없는 프로젝트")
                .description("테스트 버전 작성")
                .version("0.0.1")
        )
}