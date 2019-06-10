package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistry
import uk.co.grahamcox.goworlds.service.oauth2.clients.*
import java.util.*

/**
 * Controller for the Token endpoint for OAuth2
 */
@RestController
@RequestMapping("/oauth2/token")
class OAuth2TokenController(
        private val clientRetriever: ClientRetriever,
        private val scopeRegistry: ScopeRegistry,
        private val grantHandlers: Map<String, GrantTypeHandler>
) {
    companion object {
        /** The logger to use*/
        private val LOG = LoggerFactory.getLogger(OAuth2TokenController::class.java)

        /** Map of supported Grant Types to the enum value */
        private val GRANT_TYPES = mapOf(
                "client_credentials" to GrantType.CLIENT_CREDENTIALS,
                "refresh_token" to GrantType.REFRESH_TOKEN,
                "tag:goworlds,2019:oauth2/grant_type/email_password" to GrantType.EMAIL_PASSWORD_EXTENSION
        )

    }
    /**
     * Handle the POST method
     */
    @RequestMapping(method = [RequestMethod.POST])
    fun tokenHandler(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String?,
                     @RequestParam("grant_type") grantTypeValue: String?,
                     @RequestParam("scope") scopeValue: String?,
                     @RequestParam allParams: Map<String, String>): AccessTokenModel {
        val grantType = parseGrantType(grantTypeValue)
        val client = loadClient(authorization)

        if (!client.data.grantTypes.contains(grantType)) {
            LOG.warn("Grant Type {} is not supported by Client {}", grantType, client)
            throw OAuth2Exception(ErrorCode.UNAUTHORIZED_CLIENT, "Requested Grant Type can not be used by this client")
        }

        val scopes = scopeValue?.let(::parseScopes) ?: emptySet()

        val handler = grantHandlers[grantTypeValue]
                ?: throw OAuth2Exception(ErrorCode.UNSUPPORTED_GRANT_TYPE, "Unsupported grant type: $grantTypeValue")

        return handler.handle(client, scopes, allParams)
    }

    /**
     * Parse the Grant Type for the request
     * @param grantTypeValue The incoming value
     * @return the grant type
     */
    private fun parseGrantType(grantTypeValue: String?): GrantType {
        if (grantTypeValue.isNullOrBlank()) {
            throw OAuth2Exception(ErrorCode.INVALID_REQUEST, "No Grant Type provided")
        }

        return GRANT_TYPES[grantTypeValue]
                ?: throw OAuth2Exception(ErrorCode.UNSUPPORTED_GRANT_TYPE, "Unsupported grant type: $grantTypeValue")
    }

    /**
     * Parse the scopes that were requested
     * @param scopes The scopes string to parse
     * @return the scopes
     */
    private fun parseScopes(scopes: String): Collection<Scope> {
        val resolvedScopes = scopes.split("\\s".toRegex())
                .map(String::trim)
                .filter(String::isNotBlank)
                .toSet()
                .map { it to scopeRegistry.getScopeById(it) }

        LOG.debug("Resolved scopes: {}", resolvedScopes)

        val unknownScopes = resolvedScopes.filter { it.second == null }
                .map { it.first }

        if (unknownScopes.isNotEmpty()) {
            LOG.warn("Unknown Scopes: {}", unknownScopes)
            throw OAuth2Exception(ErrorCode.INVALID_SCOPE, "Unknown Scopes: $unknownScopes")
        }

        return resolvedScopes.mapNotNull { it.second }
    }

    /**
     * Load the OAuth2 Client details
     * @param authorization The authorization details
     * @return the client details
     */
    private fun loadClient(authorization: String?): Model<ClientId, ClientData> {
        if (authorization.isNullOrBlank()) {
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "No Client Credentials provided")
        }

        if (!authorization.startsWith("Basic ")) {
            LOG.warn("Unsupported Authorization Scheme: {}", authorization)
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "Unsupported Authorization Scheme")
        }

        val decoded = String(Base64.getDecoder().decode(authorization.substring(6)))
        val parts = decoded.split(":".toRegex(), 2)
        if (parts.size == 1) {
            LOG.warn("No Client Secret was provided")
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "No Client Secret provided")
        }
        val (clientId, clientSecret) = parts

        val client = try {
            clientRetriever.getClientById(ClientId(UUID.fromString(clientId)))
        } catch (e: UnknownClientException) {
            LOG.warn("Client ID is not a valid Client: {}", clientId)
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "Unknown Client ID and Secret")
        } catch (e: IllegalArgumentException) {
            LOG.warn("Client ID is not a valid UUID: {}", clientId)
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "Unknown Client ID and Secret")
        }

        if (client.data.secret.hash.isNotBlank() && !client.data.secret.check(clientSecret)) {
            LOG.warn("Client Secret is not valid for Client ID: {}", clientId)
            throw OAuth2Exception(ErrorCode.INVALID_CLIENT, "Unknown Client ID and Secret")
        }

        return client
    }

    /**
     * Handle when an OAuth2 Exception occurs
     */
    @ExceptionHandler(OAuth2Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOAuth2Exception(e: OAuth2Exception) = mapOf(
            "error" to e.code.code,
            "error_description" to e.message
    )

}
