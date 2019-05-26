package uk.co.grahamcox.goworlds.service.users.http

import java.net.URI
import java.time.Instant

/**
 * Representation of a User as returned over the API
 */
data class UserModel(
        val self: URI,
        val created: Instant,
        val updated: Instant,
        val name: String,
        val email: String?
)
