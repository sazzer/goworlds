package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.OpenIDConnectScopes
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Clock
import java.time.Duration

/**
 * Abstract class for the Grant Type Handlers to extend that gives common functionality
 */
abstract class AbstractGrantTypeHandler(
        private val clock: Clock,
        private val accessTokenGenerator: AccessTokenGenerator,
        private val accessTokenSerializer: AccessTokenSerializer,
        private val idTokenSerializer: IdTokenSerializer
) : GrantTypeHandler {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AbstractGrantTypeHandler::class.java)
    }

    /**
     * Get the user that this token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the user
     */
    protected abstract fun getUser(client: Model<ClientId, ClientData>,
                                   scopes: Collection<Scope>,
                                   params: Map<String, String>) : Model<UserId, UserData>

    /**
     * Handle the Grant Type
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the access token that was produced
     */
    override fun handle(client: Model<ClientId, ClientData>, scopes: Collection<Scope>, params: Map<String, String>): AccessTokenModel {
        val user = getUser(client, scopes, params)

        return buildAccessToken(user, client, scopes)
    }

    /**
     * Actually generate the Access Token for the provided caller
     * @param user The User that is the Access Token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @return the access token that was produced
     */
    private fun buildAccessToken(user: Model<UserId, UserData>,
                                 client: Model<ClientId, ClientData>,
                                 scopes: Collection<Scope>): AccessTokenModel {
        // Generate an Access Token for the User, Client and Scopes
        val accessToken = accessTokenGenerator.generate(user, client, scopes)
        LOG.debug("Generated Access Token: {}", accessToken)

        // Generate an Access Token Model for the requested details
        val now = clock.instant()
        val expiresIn = Duration.between(now, accessToken.expires).seconds

        val serializedAccessToken = accessTokenSerializer.serialize(accessToken)

        val idToken = if (scopes.contains(OpenIDConnectScopes.OPENID)) {
            idTokenSerializer.serialize(user, client, accessToken)
        } else {
            null
        }

        // And return it
        return AccessTokenModel(
                accessToken = serializedAccessToken,
                tokenType = "Bearer",
                expiry = expiresIn,
                idToken = idToken
        )
    }
}
