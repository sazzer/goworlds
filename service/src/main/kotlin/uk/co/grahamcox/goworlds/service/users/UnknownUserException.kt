package uk.co.grahamcox.goworlds.service.users

import java.lang.RuntimeException

/**
 * Exception to indicate that a user was not found
 * @property id The ID of the user that wasn't found
 */
class UnknownUserException(val id : UserId) : RuntimeException("Unknown user: $id")
