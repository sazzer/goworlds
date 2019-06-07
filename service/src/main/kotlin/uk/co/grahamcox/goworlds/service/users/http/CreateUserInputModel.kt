package uk.co.grahamcox.goworlds.service.users.http

/**
 * HTTP API Model for creating a new user
 */
data class CreateUserInputModel(
        val name: String?,
        val email: String?,
        val password: String?
)
