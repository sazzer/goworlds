package uk.co.grahamcox.goworlds.service.oauth2.http

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId

interface IdTokenSerializer {
    /**
     * Serialize the given details to an ID Token
     * @param user The User that is the ID Token is for
     * @param client The Client that is making the request
     * @param accessToken The Access Token that was generated
     * @return the ID token that was produced
     */
    fun serialize(user: Model<UserId, UserData>,
                  client: Model<ClientId, ClientData>,
                  accessToken: AccessToken) : String
}
