package dev.mfataka.parkingsystembackend.config

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project Mongo
 * @date 13.10.2025 16:02
 */
@Configuration
@ImportAutoConfiguration(LogbookAutoConfiguration::class)
class ServerBeanConfig(
    private val mongoDatabaseFactory: MongoDatabaseFactory,
    private val mongoConverter: MongoMappingContext,
    private val customConversions: MongoCustomConversions) {

    @Bean
    fun mappingMongoConverter(): MappingMongoConverter {
        val converter = MappingMongoConverter(
            DefaultDbRefResolver(mongoDatabaseFactory),
            mongoConverter
        )
        // disable class field
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        converter.setCustomConversions(customConversions)
        return converter
    }

}