package uk.co.grahamcox.goworlds.service.oauth2.http

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import uk.co.grahamcox.goworlds.service.CurrentRequest
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

@CurrentRequest
@ExtendWith(MockKExtension::class)
internal class AccessTokenInterceptorTest {
    /** The Serializer to use */
    @MockK
    private lateinit var accessTokenSerializer: AccessTokenSerializer

    /** The store to use */
    @SpyK(recordPrivateCalls = false)
    private var accessTokenStore = AccessTokenStore()

    /** The test subject */
    private lateinit var testSubject: AccessTokenInterceptor

    /** An access token */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
            created = Instant.parse("2019-05-24T18:29:00Z"),
            expires = Instant.parse("2099-05-25T18:29:00Z"),
            user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
            scopes = emptyList()
    )

    /**
     * Set up the Test Subject
     */
    @BeforeEach
    fun setup() {
        testSubject = AccessTokenInterceptor(accessTokenSerializer, accessTokenStore)

    }

    /**
     * Verify the mocks
     */
    @AfterEach
    fun verify() {
        confirmVerified(accessTokenSerializer, accessTokenStore)
    }

    @Test
    fun noAuthorizationHeader() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()

        Assertions.assertTrue(testSubject.preHandle(request, response, "Handler"))

        verify {
            // Explicitly set the access token to null
            accessTokenStore.accessToken = null
        }
    }

    @Test
    fun basicAuthorizationHeader() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Basic 123")
        val response = MockHttpServletResponse()

        Assertions.assertTrue(testSubject.preHandle(request, response, "Handler"))

        verify {
            // Explicitly set the access token to null
            accessTokenStore.accessToken = null
        }
    }

    @Test
    fun validAuthorizationHeader() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer 123")
        val response = MockHttpServletResponse()

        every { accessTokenSerializer.deserialize("123") } returns accessToken

        Assertions.assertTrue(testSubject.preHandle(request, response, "Handler"))

        verify {
            accessTokenSerializer.deserialize("123")

            // Explicitly set the access token to the deserialized value
            accessTokenStore.accessToken = accessToken
        }
    }

    @Test
    fun invalidAuthorizationHeader() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer 123")
        val response = MockHttpServletResponse()

        every { accessTokenSerializer.deserialize("123") } throws AccessTokenDeserializationException("123")

        Assertions.assertFalse(testSubject.preHandle(request, response, "Handler"))
        Assertions.assertEquals(401, response.status)

        verify {
            accessTokenSerializer.deserialize("123")

            accessTokenStore.accessToken?.wasNot(Called)
        }
    }
}
