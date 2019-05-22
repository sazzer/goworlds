package uk.co.grahamcox.goworlds.service.oauth2

import java.lang.IllegalArgumentException

/**
 * Standard implementation of the Scope Registry
 */
class ScopeRegistryImpl(private val scopes: Collection<Scope>) : ScopeRegistry {
    init {
        if (scopes.isEmpty()) {
            throw IllegalArgumentException("No scopes were provided")
        }

        val ids = scopes.map(Scope::id).toSet()
        if (ids.size < scopes.size) {
            throw IllegalArgumentException("Scopes with duplicate IDs were provided")
        }
    }

    /**
     * Get the Scope instance that has the given ID
     * @param id The ID of the scope
     * @return the Scope, or null if it wasn't found
     */
    override fun getScopeById(id: String) = scopes.find { it.id == id }
}
