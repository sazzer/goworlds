package uk.co.grahamcox.goworlds.service.oauth2.tokens

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * Mechanism by which we can generate access tokens
 */
interface AccessTokenGenerator {
    /**
     * Generate the access token for the given details
     * @param user The user to generate the token for
     * @param client The client to generate the token for
     * @param scopes The scopes to grant to the token
     * @return the token
     */
    fun generate(user: Model<UserId, UserData>,
                 client: Model<ClientId, ClientData>,
                 scopes: Collection<Scope>) : AccessToken
}
