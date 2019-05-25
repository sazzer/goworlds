package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.JWTException
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.Verifier
import io.fusionauth.jwt.domain.JWT
import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistry
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.ZoneId
import java.util.*


/**
 * Implementation of the Access Token Serializer that works in terms of a JWT
 */
class JwtAccessTokenSerializerImpl(
        private val signer: Signer,
        private val verifier: Verifier,
        private val scopeRegistry: ScopeRegistry
) : AccessTokenSerializer {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JwtAccessTokenSerializerImpl::class.java)

        /** The UTC Timezone */
        private val UTC = ZoneId.of("UTC")
    }

    /**
     * Serialize the Access Token to a String
     * @param accessToken The access token to serialize
     * @return the serialized access token
     */
    override fun serialize(accessToken: AccessToken): String {
        val jwt = JWT()
                .setIssuedAt(accessToken.created.atZone(UTC))
                .setNotBefore(accessToken.created.atZone(UTC))
                .setExpiration(accessToken.expires.atZone(UTC))
                .setSubject(accessToken.user.id.toString())
                .setAudience(accessToken.client.id.toString())
                .setIssuer(JwtAccessTokenSerializerImpl::class.java.simpleName)
                .setUniqueId(accessToken.id.id.toString())
                .addClaim("scopes", accessToken.scopes.map(Scope::id))

        return JWT.getEncoder().encode(jwt, signer)
    }

    /**
     * Deserialize an Access Token from a String
     * @param accessToken The serialized access token
     * @return the Access Token
     */
    override fun deserialize(accessToken: String): AccessToken {
        val jwt = try {
            JWT.getDecoder().decode(accessToken, verifier)
        } catch (e: JWTException) {
            LOG.warn("Failed to decode access token {}", accessToken, e)
            throw AccessTokenDeserializationException(accessToken)
        }

        val scopes = jwt.getList("scopes")
                .map(Any::toString)
                .map { it to scopeRegistry.getScopeById(it) }

        val unknownScopes = scopes.filter { it.second == null }
                .map { it.first }
        LOG.warn("Access Token {} has unknown scopes {}", accessToken, unknownScopes)

        val resolvedScopes = scopes.mapNotNull { it.second }

        return AccessToken(
                id = AccessTokenId(UUID.fromString(jwt.uniqueId)),
                created = jwt.issuedAt.toInstant(),
                expires = jwt.expiration.toInstant(),
                user = UserId(UUID.fromString(jwt.subject)),
                client = ClientId(UUID.fromString(jwt.audience.toString())),
                scopes = resolvedScopes
        )
    }
}
