package uk.co.grahamcox.goworlds.service.users.http

import java.time.Instant

/**
 * Representation of a User as returned over the API
 */
data class UserModel(
        val id: String,
        val created: Instant,
        val updated: Instant,
        val name: String,
        val email: String?
)
