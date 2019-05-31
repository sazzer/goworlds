package uk.co.grahamcox.goworlds.service.users

/**
 * Possible filters for searching users
 */
data class UserSearchFilters(
        val name: String? = null,
        val email: String? = null
)
