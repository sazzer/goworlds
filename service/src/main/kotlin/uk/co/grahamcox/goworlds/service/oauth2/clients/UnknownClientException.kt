package uk.co.grahamcox.goworlds.service.oauth2.clients

import java.lang.RuntimeException

/**
 * Exception to indicate that a client was not found
 * @property id The ID of the client that wasn't found
 */
class UnknownClientException(val id : ClientId) : RuntimeException("Unknown client: $id")
