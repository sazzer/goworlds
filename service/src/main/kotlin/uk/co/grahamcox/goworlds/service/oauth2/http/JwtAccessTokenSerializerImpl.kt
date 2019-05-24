package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import java.time.ZoneId


/**
 * Implementation of the Access Token Serializer that works in terms of a JWT
 */
class JwtAccessTokenSerializerImpl(
        private val signer: Signer
) : AccessTokenSerializer {
    companion object {
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
     * @reutrn the Access Token
     */
    override fun deserialize(accessToken: String): AccessToken {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
