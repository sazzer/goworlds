package uk.co.grahamcox.goworlds.service.oauth2.clients

import uk.co.grahamcox.goworlds.service.model.Retriever

/**
 * Interface describing how to retrieve OAuth2 Clients
 */
interface ClientRetriever : Retriever<ClientId, ClientData>