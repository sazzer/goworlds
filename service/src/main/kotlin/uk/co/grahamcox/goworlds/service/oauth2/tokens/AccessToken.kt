package uk.co.grahamcox.goworlds.service.oauth2.tokens

import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant

/**
 * Representation of an Access Token
 * @property id The ID of the Access Token
 * @property client The Client that granted the Access Token
 * @property user The User that the Access Token authenticates
 * @property scopes The scopes that the token is granted
 * @property created When the token was created
 * @property expires When the token expires
 */
data class AccessToken(
        val id: AccessTokenId,
        val client: ClientId,
        val user: UserId,
        val scopes: Collection<Scope>,
        val created: Instant,
        val expires: Instant
)
