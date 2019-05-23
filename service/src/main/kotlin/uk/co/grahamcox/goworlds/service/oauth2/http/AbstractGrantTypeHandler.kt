package uk.co.grahamcox.goworlds.service.oauth2.http

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * Abstract class for the Grant Type Handlers to extend that gives common functionality
 */
abstract class AbstractGrantTypeHandler : GrantTypeHandler {
    /**
     * Actually generate the Access Token for the provided caller
     * @param user The User that is the Access Token is for
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @return the access token that was produced
     */
    protected fun buildAccessToken(user: Model<UserId, UserData>,
                                   client: Model<ClientId, ClientData>,
                                   scopes: Collection<Scope>): AccessTokenModel {
        // TODO: Generate an Access Token for the User, Client and Scopes
        // TODO: Generate an Access Token Model for the requested details
        // And return it
        return AccessTokenModel(
                accessToken = "accessToken",
                tokenType = "Bearer",
                expiry = 3600
        )
    }
}
