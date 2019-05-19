package uk.co.grahamcox.goworlds.service.oauth2.clients

import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UserId
import java.net.URI

/**
 * Data representing an OAuth2 Client
 */
data class ClientData(
        val name: String,
        val secret: HashedPassword,
        val owner: UserId,
        val redirectUris: Set<URI>,
        val responseTypes: Set<ResponseType>,
        val grantTypes: Set<GrantType>
)
