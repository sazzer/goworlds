package uk.co.grahamcox.goworlds.service.users

import java.lang.RuntimeException

/**
 * Exception to indicate creating or updating a user with a duplicate email address
 */
class DuplicateEmailException(val email: String) : RuntimeException("Email Address is already in use: $email")
