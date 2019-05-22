package uk.co.grahamcox.goworlds.service.oauth2

/**
 * Registry of scopes that the system knows about
 */
interface ScopeRegistry {
    /**
     * Get the Scope instance that has the given ID
     * @param id The ID of the scope
     * @return the Scope, or null if it wasn't found
     */
    fun getScopeById(id: String) : Scope?
}
