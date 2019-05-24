package uk.co.grahamcox.goworlds.service.oauth2.tokens

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Clock
import java.time.Duration
import java.util.*

/**
 * Standard implementation of the Access Token Generator
 */
class AccessTokenGeneratorImpl(private val clock: Clock, private val duration: Duration) : AccessTokenGenerator {
    /**
     * Generate the access token for the given details
     * @param user The user to generate the token for
     * @param client The client to generate the token for
     * @param scopes The scopes to grant to the token
     * @return the token
     */
    override fun generate(user: Model<UserId, UserData>,
                          client: Model<ClientId, ClientData>,
                          scopes: Collection<Scope>): AccessToken {
        val id = AccessTokenId(UUID.randomUUID())
        val now = clock.instant()
        val expires = now.plus(duration)

        return AccessToken(
                id = id,
                created = now,
                expires = expires,
                client = client.identity.id,
                user = user.identity.id,
                scopes = scopes
        )
    }
}
