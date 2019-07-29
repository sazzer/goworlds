package uk.co.grahamcox.goworlds.service.integration.database

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Spring configuration for the database
 */
@Configuration
class DatabaseConfig {
    /**
     * The Embedded Postgres server
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun embeddedPostgres() = PostgresWrapper()

    /**
     * The data source to use
     */
    @Bean
    fun datasource(postgres: PostgresWrapper) = DataSourceBuilder.create()
            .url(postgres.url)
            .username("worlds")
            .password("worlds")
            .build()
}
