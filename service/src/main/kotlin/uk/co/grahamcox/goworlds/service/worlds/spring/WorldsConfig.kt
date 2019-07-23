package uk.co.grahamcox.goworlds.service.worlds.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.goworlds.service.worlds.dao.WorldJdbcDao

/**
 * Spring Configuration for working with Worlds
 */
@Configuration
class WorldsConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<WorldJdbcDao>()
        }.initialize(context)
    }
}
