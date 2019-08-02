package uk.co.grahamcox.goworlds.service.worlds

import java.lang.RuntimeException

/**
 * Exception to indicate creating or updating a world with a duplicate URL Slug
 */
class DuplicateSlugException(val slug: String) : RuntimeException("URL Slug is already in use: $slug")
