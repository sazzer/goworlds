package uk.co.grahamcox.goworlds.service.oauth2.http

import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken

/**
 * Implementation of the Access Token Serializer that works in terms of a JWT
 */
class JwtAccessTokenSerializerImpl : AccessTokenSerializer {
    /**
     * Serialize the Access Token to a String
     * @param accessToken The access token to serialize
     * @return the serialized access token
     */
    override fun serialize(accessToken: AccessToken): String {
        return "Hello"
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
