package uk.co.grahamcox.goworlds.service.oauth2.http

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HTTP Representation of an Access Token
 */
data class AccessTokenModel(
        @JsonProperty("access_token")
        val accessToken: String,

        @JsonProperty("token_type")
        val tokenType: String,

        @JsonProperty("expires_in")
        val expiry: Int,

        @JsonProperty("scope")
        val scope: String? = null,

        @JsonProperty("state")
        val state: String? = null
)
