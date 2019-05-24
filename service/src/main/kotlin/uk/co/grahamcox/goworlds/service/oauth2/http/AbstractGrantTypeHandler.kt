package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Clock
import java.time.Duration
import java.time.Period

/**
 * Abstract class for the Grant Type Handlers to extend that gives common functionality
 */
abstract class AbstractGrantTypeHandler(
        private val clock: Clock,
        private val accessTokenGenerator: AccessTokenGenerator
) : GrantTypeHandler {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AbstractGrantTypeHandler::class.java)
    }

    /**
     * Actually generate the Access Token for the provided caller
     * @param user The User that is the Access Token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @return the access token that was produced
     */
    protected fun buildAccessToken(user: Model<UserId, UserData>,
                                   client: Model<ClientId, ClientData>,
                                   scopes: Collection<Scope>): AccessTokenModel {
        // Generate an Access Token for the User, Client and Scopes
        val accessToken = accessTokenGenerator.generate(user, client, scopes)
        LOG.debug("Generated Access Token: {}", accessToken)

        // TODO: Generate an Access Token Model for the requested details
        // And return it

        val now = clock.instant()
        val expiresIn = Duration.between(now, accessToken.expires).seconds

        return AccessTokenModel(
                accessToken = "accessToken",
                tokenType = "Bearer",
                expiry = expiresIn
        )
    }
}
