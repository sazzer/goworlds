package uk.co.grahamcox.goworlds.service.integration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseConfig

/**
 * Spring configuration for the integration tests
 */
@Configuration
@Import(
        DatabaseConfig::class
)
class IntegrationTestConfig
