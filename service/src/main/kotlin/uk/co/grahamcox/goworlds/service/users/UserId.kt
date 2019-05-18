package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Id
import java.util.*

/**
 * The ID of a User
 */
data class UserId(override val id: UUID) : Id
