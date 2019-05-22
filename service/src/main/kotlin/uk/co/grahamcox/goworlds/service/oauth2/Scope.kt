package uk.co.grahamcox.goworlds.service.oauth2

/**
 * Interface that all scopes need to implement
 */
interface Scope {
    /** The ID o the scope */
    val id: String
}
