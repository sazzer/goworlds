package uk.co.grahamcox.goworlds.service.acceptance.oauth2

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.net.URI

/**
 * Tests for the Controllers that receive Access Tokens
 */
class AccessTokenIT : TokenTestBase() {
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

    @Test
    fun successfulAccessToken() {
        val request = RequestEntity<Unit>(HttpMethod.GET, URI("/oauth2/token/details/required"))
        val response = makeRequest(client, request, Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) }
        )
    }

    @Test
    fun requiredMissingAccessToken() {
        val request = RequestEntity<Unit>(HttpMethod.GET, URI("/oauth2/token/details/required"))
        val response = restTemplate.exchange(request, Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) }
        )
    }

    @Test
    fun optionalMissingAccessToken() {
        val request = RequestEntity<Unit>(HttpMethod.GET, URI("/oauth2/token/details"))
        val response = restTemplate.exchange(request, Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) }
        )
    }

    @Test
    fun requiredInvalidAccessToken() {
        val request = RequestEntity<Unit>(HttpMethod.GET, URI("/oauth2/token/details/required"))
        val response = makeRequest("Invalid", request, Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) }
        )
    }

    @Test
    fun optionalInvalidAccessToken() {
        val request = RequestEntity<Unit>(HttpMethod.GET, URI("/oauth2/token/details"))
        val response = makeRequest("Invalid", request, Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) }
        )
    }
}
