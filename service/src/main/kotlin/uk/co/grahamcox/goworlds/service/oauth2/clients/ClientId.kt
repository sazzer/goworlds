package uk.co.grahamcox.goworlds.service.oauth2.clients

import uk.co.grahamcox.goworlds.service.model.Id
import java.util.*

/**
 * The ID of an OAuth2 Client
 */
data class ClientId(override val id: UUID) : Id
