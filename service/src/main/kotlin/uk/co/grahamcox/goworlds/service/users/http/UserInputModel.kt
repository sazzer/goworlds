package uk.co.grahamcox.goworlds.service.users.http

/**
 * HTTP API Model for creating or updating a user
 */
data class UserInputModel(
        val name: String?,
        val email: String?,
        val password: String?
)
