package uk.co.grahamcox.goworlds.service.oauth2.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.goworlds.service.oauth2.OpenIDConnectScopes
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistry
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistryImpl
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientJdbcDao
import uk.co.grahamcox.goworlds.service.oauth2.http.OAuth2TokenController

/**
 * Spring Configuration for working with OAuth2 Details
 */
@Configuration
class OAuth2Config(context: GenericApplicationContext) {
    init {
        beans {
            bean<ClientJdbcDao>()
            bean<ScopeRegistry> {
                ScopeRegistryImpl(
                        listOf(
                            OpenIDConnectScopes.values().asList()
                        ).flatten()
                )
            }
            bean<OAuth2TokenController>()
        }.initialize(context)
    }
}
