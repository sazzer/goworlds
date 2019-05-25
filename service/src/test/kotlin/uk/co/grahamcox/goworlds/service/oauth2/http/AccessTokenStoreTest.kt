package uk.co.grahamcox.goworlds.service.oauth2.http

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.goworlds.service.CurrentRequest
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Access Token Store
 */
@CurrentRequest
internal class AccessTokenStoreTest {
    /** The test subject */
    private val testSubject = AccessTokenStore()

    /** An access token */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
            created = Instant.parse("2019-05-24T18:29:00Z"),
            expires = Instant.parse("2099-05-25T18:29:00Z"),
            user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
            scopes = emptyList()
    )

    @Test
    fun getWhenNotSet() {
        Assertions.assertNull(testSubject.accessToken)
    }

    @Test
    fun setThenGet() {
        testSubject.accessToken = accessToken
        Assertions.assertSame(accessToken, testSubject.accessToken)
    }

    @Test
    fun setRemoveThenGet() {
        testSubject.accessToken = accessToken
        Assertions.assertSame(accessToken, testSubject.accessToken)

        testSubject.accessToken = null
        Assertions.assertNull(testSubject.accessToken)
    }

}
