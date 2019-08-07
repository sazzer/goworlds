package uk.co.grahamcox.goworlds.service.acceptance.worlds

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.http.*
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.dao.WorldSeed
import java.net.URI
import java.util.*

/**
 * Acceptance tests for updating a world
 */
class UpdateWorldIT : IntegrationTestBase() {
    /** A user to use as the owner of the worlds */
    private lateinit var owner: UserSeed

    /** A client to work as */
    private var client: ClientSeed? = null

    /** A world to work with */
    private lateinit var world: WorldSeed

    /**
     * Create the common data
     */
    @BeforeEach
    fun createData() {
        owner = seed(UserSeed())
        client = seed(ClientSeed(ownerId = owner.id))
        world = seed(WorldSeed(
                ownerId = owner.id,
                name = "Original Name",
                description = "Original Description",
                slug = "original-world"
        ))
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
                mapOf("name" to "", "description" to "Test World", "slug" to "test-world") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",
                mapOf("name" to "Graham", "description" to "", "slug" to "test-world") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "description"
                }""",
                mapOf("name" to "Graham", "description" to "graham@grahamcox.co.uk", "slug" to "") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "slug"
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
    fun updateSuccess() {
        val response = makeRequest(client, world.id.toString(), mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertNotEquals(""""${world.version}"""", response.headers.eTag) },
                Executable { Assertions.assertEquals("/worlds/${world.id}", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) },

                Executable { Assertions.assertEquals("Test World", response.body?.get("name")) },
                Executable { Assertions.assertEquals("Test World", response.body?.get("description")) },
                Executable { Assertions.assertEquals("test-world", response.body?.get("slug")) },
                Executable { Assertions.assertEquals(world.id.toString(), response.body?.get("id")) }
        )
    }

    @Test
    fun updateSuccessMinimal() {
        val response = makeRequest(client, world.id.toString(), mapOf(
                "description" to "Test World"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertNotEquals(""""${world.version}"""", response.headers.eTag) },
                Executable { Assertions.assertEquals("/worlds/${world.id}", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) },

                Executable { Assertions.assertEquals("Original Name", response.body?.get("name")) },
                Executable { Assertions.assertEquals("Test World", response.body?.get("description")) },
                Executable { Assertions.assertEquals("original-world", response.body?.get("slug")) },
                Executable { Assertions.assertEquals(world.id.toString(), response.body?.get("id")) }
        )
    }

    @Test
    fun updateSuccessReRetrieve() {
        val updated = makeRequest(client, world.id.toString(), mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, updated.statusCode) },
                Executable { Assertions.assertTrue(updated.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) }
        )

        val retrieved = restTemplate.getForEntity("/worlds/${world.id}", Map::class.java)

        Assertions.assertEquals(retrieved.body, updated.body)
    }

    @Test
    fun updateUnauthorized() {
        val response = makeRequest(null, world.id.toString(), mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun updateWrongOwner() {
        val anotherUser = seed(UserSeed(email = "another@example.com"))
        val anotherClient = seed(ClientSeed(ownerId = anotherUser.id))

        val response = makeRequest(anotherClient, world.id.toString(), mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    /**
     * Actually make the request to the API
     */
    private fun makeRequest(clientSeed: ClientSeed?,
                            worldId: String,
                            input: Map<String, Any>?): ResponseEntity<out Map<*, *>> {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.parseMediaType("application/merge-patch+json")

        val request = RequestEntity(input, requestHeaders, HttpMethod.PATCH, URI("/worlds/$worldId"))

        return makeRequest(clientSeed, request, Map::class.java)
    }
}
