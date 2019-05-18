package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.password.HashedPassword

/**
 * The data that makes up a User
 * @property name The name of the user
 * @property email The email address of the user
 * @property password The hashed password of the user
 */
data class UserData(
        val name: String,
        val email: String,
        val password: HashedPassword
)
