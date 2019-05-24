package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.users.UserRetriever
import java.time.Clock

/**
 * Grant Type Handler for a Client Credentials Grant
 */
class ClientCredentialsGrantTypeHandler(
        private val userRetriever: UserRetriever,
        clock: Clock,
        accessTokenGenerator: AccessTokenGenerator
) : AbstractGrantTypeHandler(clock, accessTokenGenerator) {
    companion object {
        /** The logger to use*/
        private val LOG = LoggerFactory.getLogger(ClientCredentialsGrantTypeHandler::class.java)
    }

    /**
     * Handle the Grant Type
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the access token that was produced
     */
    override fun handle(client: Model<ClientId, ClientData>, scopes: Collection<Scope>, params: Map<String, String>): AccessTokenModel {
        // Load the user to generate the access token for - the owner of the OAuth2 Client details
        val user = userRetriever.getUserById(client.data.owner)

        LOG.debug("Performing a Client Credentials Grant for user {} and client {} with scopes {}", user, client, scopes)

        return buildAccessToken(user, client, scopes)
    }
}
