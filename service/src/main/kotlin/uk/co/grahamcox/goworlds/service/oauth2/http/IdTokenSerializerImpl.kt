package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.ZoneId

/**
 * Mechanism to serialize the authentication details to an ID Token
 */
class IdTokenSerializerImpl(private val signer: Signer) : IdTokenSerializer {
    companion object {
        /** The UTC Timezone */
        private val UTC = ZoneId.of("UTC")
    }

    /**
     * Serialize the given details to an ID Token
     * @param user The User that is the ID Token is for
     * @param client The Client that is making the request
     * @param accessToken The Access Token that was generated
     * @return the ID token that was produced
     */
    override fun serialize(user: Model<UserId, UserData>,
                  client: Model<ClientId, ClientData>,
                  accessToken: AccessToken) : String {

        val jwt = JWT()
                .setIssuer(IdTokenSerializerImpl::class.java.simpleName)
                .setSubject(user.identity.id.id.toString())
                .setAudience(client.identity.id.id.toString())
                .setExpiration(accessToken.expires.atZone(UTC))
                .setIssuedAt(accessToken.created.atZone(UTC))
                .addClaim("auth_time", accessToken.created.atZone(UTC))
        return JWT.getEncoder().encode(jwt, signer)
    }
}
