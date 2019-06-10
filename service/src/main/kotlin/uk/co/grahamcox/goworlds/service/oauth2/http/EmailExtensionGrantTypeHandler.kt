package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.UserRetriever
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

        val user = try {
            userRetriever.getUserByEmail(email)
        } catch (e: UnknownUserException) {
            LOG.info("Authentication attempt from unknown email address: {}", email)
            throw OAuth2Exception(ErrorCode.ACCESS_DENIED, "Unknown email address: $email")
        }

        if (!user.data.password.check(password)) {
            LOG.info("Authentication attempt with incorrect password: {}", email)
            throw OAuth2Exception(ErrorCode.ACCESS_DENIED, "Invalid password or account blocked")
        }

        return user
    }
}
