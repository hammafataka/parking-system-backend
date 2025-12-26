package dev.mfataka.parkingsystembackend.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.logbook.BodyFilter
import org.zalando.logbook.Logbook
import org.zalando.logbook.core.attributes.JwtAllMatchingClaimsExtractor
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter
import org.zalando.logbook.spring.webflux.LogbookWebFilter
import tools.jackson.databind.json.JsonMapper

@Configuration
class LogbookConfig {

    @Bean
    fun logbookWebFilter(@Qualifier("jsonBodyFieldsFilter") filter: BodyFilter, jsonMapper: JsonMapper): LogbookWebFilter {
        val logbook = Logbook.builder()
            .bodyFilter(filter)
            .attributeExtractor(JwtAllMatchingClaimsExtractor(jsonMapper, mutableListOf()))
            .build()
        return LogbookWebFilter(logbook)
    }


    @Bean
    fun prettyJsonBodyFilter(): BodyFilter = PrettyPrintingJsonBodyFilter()
}