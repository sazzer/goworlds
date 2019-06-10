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
import uk.co.grahamcox.goworlds.service.users.UserSearchFilters
import java.lang.UnsupportedOperationException
import java.time.Clock

/**
 * Grant Type Handler for an Email/Password Extension Grant
 */
class EmailExtensionGrantTypeHandler(
        private val userRetriever: UserRetriever,
        clock: Clock,
        accessTokenGenerator: AccessTokenGenerator,
        accessTokenSerializer: AccessTokenSerializer
) : AbstractGrantTypeHandler(clock, accessTokenGenerator, accessTokenSerializer) {
    companion object {
        /** The logger to use*/
        private val LOG = LoggerFactory.getLogger(EmailExtensionGrantTypeHandler::class.java)
    }

    /**
     * Get the user that this token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the user
     */
    override fun getUser(client: Model<ClientId, ClientData>, scopes: Collection<Scope>, params: Map<String, String>): Model<UserId, UserData> {
        val email = params["email"]
        if (email.isNullOrBlank()) {
            throw OAuth2Exception(ErrorCode.INVALID_REQUEST, "Required field was not present: email")
        }
        
        val password = params["password"]
        if (password.isNullOrBlank()) {
            throw OAuth2Exception(ErrorCode.INVALID_REQUEST, "Required field was not present: password")
        }

        val users = userRetriever.searchUsers(filters = UserSearchFilters(email = email),
                offset = 0,
                count = 1)

        if (users.entries.isEmpty()) {
            LOG.info("Authentication attempt from unknown email address: {}", email)
            throw OAuth2Exception(ErrorCode.ACCESS_DENIED, "Unknown email address: $email")
        }

        val user = users.entries[0]

        if (!user.data.password.check(password)) {
            LOG.info("Authentication attempt with incorrect password: {}", email)
            throw OAuth2Exception(ErrorCode.ACCESS_DENIED, "Invalid password or account blocked")
        }

        return user
    }
}
