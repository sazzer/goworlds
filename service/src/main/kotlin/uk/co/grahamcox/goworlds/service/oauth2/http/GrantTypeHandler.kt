package uk.co.grahamcox.goworlds.service.oauth2.http

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.Scope
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId

/**
 * Interface for how to handle a particular Grant Type
 */
interface GrantTypeHandler {
    /**
     * Handle the Grant Type
     * @param client The Client that is making the request
     * @param scopes The Scopes that are requested
     * @param params The parameters to the request
     * @return the access token that was produced
     */
    fun handle(client: Model<ClientId, ClientData>,
               scopes: Collection<Scope>,
               params: Map<String, String>) : AccessTokenModel
}
