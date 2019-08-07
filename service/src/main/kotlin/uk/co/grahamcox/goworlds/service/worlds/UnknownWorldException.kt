package uk.co.grahamcox.goworlds.service.worlds

import java.lang.RuntimeException

/**
 * Exception to indicate that a world was not found
 * @property id The Identifier of the world that wasn't found
 */
class UnknownWorldException(val id : Any?) : RuntimeException("Unknown world: $id")
