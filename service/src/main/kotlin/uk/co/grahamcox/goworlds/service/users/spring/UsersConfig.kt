package uk.co.grahamcox.goworlds.service.users.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.goworlds.service.users.dao.UserJdbcDao

/**
 * Spring Configuration for working with Users
 */
@Configuration
class UsersConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<UserJdbcDao>()
        }.initialize(context)
    }
}
