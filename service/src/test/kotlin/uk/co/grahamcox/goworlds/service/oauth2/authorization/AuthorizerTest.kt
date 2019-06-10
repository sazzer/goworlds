package uk.co.grahamcox.goworlds.service.oauth2.authorization

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Authorizer
 */
internal class AuthorizerTest {
    /** The access token to use */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
            created = Instant.parse("2019-05-24T18:29:00Z"),
            expires = Instant.parse("2099-05-25T18:29:00Z"),
            user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
            scopes = emptyList()
    )

    /** The test subject */
    private val testSubject = Authorizer(accessToken)

    @Test
    fun testSameUser() {
        testSubject {
            sameUser(accessToken.user)
        }
    }

    @Test
    fun testNotSameUser() {
        Assertions.assertThrows(AuthorizationFailedException::class.java) {
            testSubject {
                sameUser(UserId(UUID.randomUUID()))
            }
        }
    }
}
