package uk.co.grahamcox.goworlds.service.oauth2.http

import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken

/**
 * Mechanism to convert an Access Token to a String and back
 */
interface AccessTokenSerializer {
    /**
     * Serialize the Access Token to a String
     * @param accessToken The access token to serialize
     * @return the serialized access token
     */
    fun serialize(accessToken: AccessToken) : String

    /**
     * Deserialize an Access Token from a String
     * @param accessToken The serialized access token
     * @reutrn the Access Token
     */
    fun deserialize(accessToken: String) : AccessToken
}
