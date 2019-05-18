package uk.co.grahamcox.goworlds.service.integration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseCleaner
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseConfig

/**
 * Spring configuration for the integration tests
 */
@Configuration
@Import(
        DatabaseConfig::class
)
class IntegrationTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean {
                DatabaseCleaner(
                        ref(),
                        listOf(
                                "flyway_schema_history"
                        )
                )
            }
        }.initialize(context)
    }
}
