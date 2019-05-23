package uk.co.grahamcox.goworlds.service.oauth2.tokens

import uk.co.grahamcox.goworlds.service.model.Id
import java.util.*

/**
 * The ID of an Access Token
 */
data class AccessTokenId(override val id: UUID) : Id
