package uk.co.grahamcox.goworlds.service.acceptance.oauth2

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.util.*

/**
 * Tests for the OAuth2 Token Controller doing a Client Credentials auth
 */
class ClientCredentialsIT : TokenTestBase() {
    /** The seeded user */
    private lateinit var user: UserSeed

    /** The seeded OAuth2 Client */
    private lateinit var client: ClientSeed

    /**
     * Create the seed data
     */
    @BeforeEach
    fun createData() {
        user = seed(UserSeed())
        client = seed(ClientSeed(ownerId = user.id, grantTypes = setOf(GrantType.CLIENT_CREDENTIALS)))
    }

    @TestFactory
    fun clientAuthenticationFailure(): List<DynamicTest> {
        data class TestData(val name: String, val authorization: String?)

        val params = mapOf(
                "grant_type" to listOf("client_credentials")
        )

        return listOf(
                TestData("No Authorization Header", null),
                TestData("Unsupported Authorization Scheme", "Other abc"),
                TestData("No Client Secret", basicEncode(client.id.toString())),
                TestData("Malformed Client ID", basicEncode("abc:def")),
                TestData("Incorrect Client ID", basicEncode("${UUID.randomUUID()}:def")),
                TestData("Incorrect Client Secret", basicEncode("${client.id}:${UUID.randomUUID()}"))
        ).map { testData ->
            DynamicTest.dynamicTest(testData.name) {
                val headers = mapOf("Authorization" to testData.authorization)
                        .filter { it.value != null }
                        .mapValues { it.value!! }

                val response = makeRequest(params, headers)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                        Executable { Assertions.assertEquals("invalid_client", response.body!!["error"]) }
                )
            }
        }
    }

    @Test
    fun unsupportedGrantType() {
        val client = seed(ClientSeed(ownerId = user.id, grantTypes = setOf(GrantType.REFRESH_TOKEN)))

        val params = mapOf(
                "grant_type" to listOf("client_credentials")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("unauthorized_client", response.body!!["error"]) }
        )
    }

    @Test
    fun unknownScopes() {
        val params = mapOf(
                "grant_type" to listOf("client_credentials"),
                "scope" to listOf("openid unknown")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_scope", response.body!!["error"]) }
        )
    }

    @Test
    fun successfulAccessToken() {
        val params = mapOf(
                "grant_type" to listOf("client_credentials")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertNotNull(response.body!!["access_token"]) },
                Executable { Assertions.assertEquals("Bearer", response.body!!["token_type"]) },
                Executable { Assertions.assertNotNull(response.body!!["expires_in"]) },
                Executable { Assertions.assertNull(response.body!!["id_token"]) }
        )
    }

    @Test
    fun successfulAccessIdToken() {
        val params = mapOf(
                "grant_type" to listOf("client_credentials"),
                "scope" to listOf("openid")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertNotNull(response.body!!["access_token"]) },
                Executable { Assertions.assertEquals("Bearer", response.body!!["token_type"]) },
                Executable { Assertions.assertNotNull(response.body!!["expires_in"]) },
                Executable { Assertions.assertNotNull(response.body!!["id_token"]) }
        )
    }

    /**
     * Helper to encode the given string as HTTP Basic Auth
     * @param input the input to encode
     * @return the header value to use
     */
    private fun basicEncode(input: String) =
            "Basic " + Base64.getEncoder().encodeToString(input.toByteArray())
}
