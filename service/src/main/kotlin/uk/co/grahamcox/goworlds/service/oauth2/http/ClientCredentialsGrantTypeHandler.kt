package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.UserRetriever
import java.time.Clock

/**
 * Grant Type Handler for a Client Credentials Grant
 */
class ClientCredentialsGrantTypeHandler(
        private val userRetriever: UserRetriever,
        clock: Clock,
        accessTokenGenerator: AccessTokenGenerator,
        accessTokenSerializer: AccessTokenSerializer,
        idTokenSerializer: IdTokenSerializer
) : AbstractGrantTypeHandler(clock, accessTokenGenerator, accessTokenSerializer, idTokenSerializer) {
    companion object {
        /** The logger to use*/
        private val LOG = LoggerFactory.getLogger(ClientCredentialsGrantTypeHandler::class.java)
    }

    /**
     * Get the user that this token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the user
     */
    override fun getUser(client: Model<ClientId, ClientData>, scopes: Collection<Scope>, params: Map<String, String>): Model<UserId, UserData> {
        val user = userRetriever.getById(client.data.owner)

        ClientCredentialsGrantTypeHandler.LOG.debug("Performing a Client Credentials Grant for user {} and client {} with scopes {}", user, client, scopes)

        return user
    }
}
