package uk.co.grahamcox.goworlds.service.acceptance.users

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.http.*
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.net.URI
import java.util.*

/**
 * Acceptance tests for updating a user
 */
class UpdateUserIT : IntegrationTestBase() {
    /** A user to work as */
    private lateinit var user: UserSeed

    /** A client to work as */
    private lateinit var client: ClientSeed

    /**
     * Create the common data
     */
    @BeforeEach
    fun createData() {
        user = seed(UserSeed(
                name = "Original",
                email = "original@example.com"
        ))
        client = seed(ClientSeed(ownerId = user.id))
    }

    @TestFactory
    fun createInvalidRequest(): List<DynamicTest> {
        return listOf(
                // No Body
                null to """{
                    "type": "tag:goworlds,2019:problems/missing-request-body",
                    "title": "The request body was required but not present",
                    "status": 400
                }""",

                // Blank Fields
                mapOf("name" to "", "email" to "graham@grahamcox.co.uk", "password" to "secret") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",
                mapOf("name" to "Graham", "email" to "", "password" to "secret") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "email"
                }""",
                mapOf("name" to "Graham", "email" to "graham@grahamcox.co.uk", "password" to "") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "password"
                }"""
        ).map { (input, expected) ->
            DynamicTest.dynamicTest("Posting: $input") {
                val response = makeRequest(client, UUID.randomUUID().toString(), input)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                        Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                        Executable { assertJson(expected, response.body!!) }
                )
            }
        }
    }

    @Test
    fun updateDuplicateEmail() {
        val user2 = seed(UserSeed(email = "graham2@grahamcox.co.uk"))

        val response = makeRequest(client, user.id.toString(), mapOf(
                "email" to user2.email
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:users/problems/duplicate-email-address",
                    "title": "The provided email address is already in use",
                    "status": 409
                }""", response.body!!) }
        )
    }

    @Test
    fun updateSuccess() {
        val response = makeRequest(client, user.id.toString(), mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertNotEquals(""""${user.version}"""", response.headers.eTag) },
                Executable { Assertions.assertEquals("/users/${user.id}", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) },

                Executable { Assertions.assertEquals("Graham", response.body?.get("name")) },
                Executable { Assertions.assertEquals("graham@grahamcox.co.uk", response.body?.get("email")) },
                Executable { Assertions.assertEquals("/users/${user.id}", response.body?.get("self")) }
        )
    }

    @Test
    fun createSuccessReRetrieve() {
        val updated = makeRequest(client, user.id.toString(), mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, updated.statusCode) },
                Executable { Assertions.assertTrue(updated.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) }
        )

        val retrieved = restTemplate.getForEntity("/users/${user.id}", Map::class.java)

        Assertions.assertEquals(retrieved.body, updated.body)
    }

    @Test
    fun updateNoToken() {
        val response = makeRequest(null, user.id.toString(), mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk"
        ))

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun updateWrongUser() {
        val response = makeRequest(client, UUID.randomUUID().toString(), mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk"
        ))

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    /**
     * Actually make the request to the API
     */
    private fun makeRequest(clientSeed: ClientSeed?,
                            userId: String,
                            input: Map<String, Any>?): ResponseEntity<out Map<*, *>> {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.parseMediaType("application/merge-patch+json")

        val request = RequestEntity(input, requestHeaders, HttpMethod.PATCH, URI("/users/$userId"))

        return makeRequest(clientSeed, request, Map::class.java)
    }
}
