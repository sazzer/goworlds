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
class EmailExtensionIT : TokenTestBase() {
    /** The seeded user */
    private lateinit var user: UserSeed

    /** The seeded OAuth2 Client */
    private lateinit var client: ClientSeed

    /**
     * Create the seed data
     */
    @BeforeEach
    fun createData() {
        user = seed(UserSeed(email = "graham@grahamcox.co.uk", password = "superSecret"))
        client = seed(ClientSeed(ownerId = user.id, grantTypes = setOf(GrantType.EMAIL_PASSWORD_EXTENSION)))
    }

    @Test
    fun successfulAccessToken() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(user.email),
                "password" to listOf(user.password)
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
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(user.email),
                "password" to listOf(user.password),
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

    @Test
    fun missingEmail() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "password" to listOf("incorrect")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    @Test
    fun missingPassword() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(user.email)
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    @Test
    fun blankEmail() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(""),
                "password" to listOf("incorrect")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    @Test
    fun blankPassword() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(user.email),
                "password" to listOf("")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    @Test
    fun invalidPassword() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf(user.email),
                "password" to listOf("incorrect")
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("access_denied", response.body!!["error"]) }
        )
    }

    @Test
    fun unknownEmail() {
        val params = mapOf(
                "grant_type" to listOf("tag:goworlds,2019:oauth2/grant_type/email_password"),
                "email" to listOf("unknown"),
                "password" to listOf(user.password)
        )

        val headers = mapOf("Authorization" to basicEncode("${client.id}:${client.secret}"))

        val response = makeRequest(params, headers)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("access_denied", response.body!!["error"]) }
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
