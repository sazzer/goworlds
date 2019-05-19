package uk.co.grahamcox.goworlds.service.oauth2.clients

import uk.co.grahamcox.goworlds.service.model.Model

/**
 * Interface describing how to retrieve OAuth2 Clients
 */
interface ClientRetriever {
    /**
     * Get the client with the given ID
     * @param id The ID of the client
     * @return the client details
     */
    fun getClientById(id : ClientId) : Model<ClientId, ClientData>
}
