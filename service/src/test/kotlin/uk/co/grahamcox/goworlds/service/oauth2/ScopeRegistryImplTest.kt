package uk.co.grahamcox.goworlds.service.oauth2

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

/**
 * Unit tests for the Scope Registry
 */
internal class ScopeRegistryImplTest {
    /** The test subject */
    private val testSubject = ScopeRegistryImpl(OpenIDConnectScopes.values().toList())

    @Test
    fun getUnknownScopeById() {
        Assertions.assertNull(testSubject.getScopeById("unknown"))
    }

    @Test
    fun getKnownScopeById() {
        Assertions.assertEquals(OpenIDConnectScopes.PROFILE, testSubject.getScopeById(OpenIDConnectScopes.PROFILE.id))
    }

    @Test
    fun constructNoScopes() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ScopeRegistryImpl(emptyList())
        }
    }

    @Test
    fun constructDuplicateScopes() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ScopeRegistryImpl(listOf(OpenIDConnectScopes.PROFILE, OpenIDConnectScopes.PROFILE))
        }
    }
}
