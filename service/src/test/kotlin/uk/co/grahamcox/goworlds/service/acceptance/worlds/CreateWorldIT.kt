package uk.co.grahamcox.goworlds.service.acceptance.worlds

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.http.*
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.dao.WorldSeed
import java.net.URI

/**
 * Acceptance tests for creating a world
 */
class CreateWorldIT : IntegrationTestBase() {
    /** The owner of the worlds */
    private lateinit var owner: UserSeed

    /** A client to work as */
    private var client: ClientSeed? = null

    /**
     * Seed a new user to own the worlds
     */
    @BeforeEach
    fun seedOwner() {
        owner = seed(UserSeed())
        client = seed(ClientSeed(ownerId = owner.id))
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

                // No fields
                emptyMap<String, Any>() to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",

                // Only unexpected fields
                mapOf("unexpected" to "field") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",

                // Missing Fields
                mapOf("description" to "Test World", "slug" to "test-world") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",
                mapOf("name" to "Test World", "slug" to "test-world") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "description"
                }""",
                mapOf("name" to "Graham", "description" to "Test World") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "slug"
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
                val response = makeRequest(input)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                        Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                        Executable { assertJson(expected, response.body!!) }
                )
            }
        }
    }

    @Test
    fun createDuplicateSlug() {
        seed(WorldSeed(ownerId = owner.id, slug = "test-world"))

        val response = makeRequest(mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:worlds/problems/duplicate-slug",
                    "title": "The provided URL Slug is already in use",
                    "status": 409
                }""", response.body!!) }
        )
    }

    @Test
    fun createUnauthenticted() {
        client = null

        val response = makeRequest(mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun createSuccess() {
        val response = makeRequest(mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertEquals("Test World", response.body?.get("name")) },
                Executable { Assertions.assertEquals("Test World", response.body?.get("description")) },
                Executable { Assertions.assertEquals("test-world", response.body?.get("slug")) },
                Executable { Assertions.assertEquals("/worlds/${response.body?.get("id")}", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) }
        )
    }

    @Test
    fun createSuccessReRetrieve() {
        val created = makeRequest(mapOf(
                "name" to "Test World",
                "description" to "Test World",
                "slug" to "test-world"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, created.statusCode) },
                Executable { Assertions.assertTrue(created.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) }
        )

        val id = created.body!!["id"].toString()
        val retrieved = restTemplate.getForEntity("/worlds/$id", Map::class.java)

        Assertions.assertEquals(retrieved.body, created.body)

    }

    /**
     * Actually make the request to the API
     */
    private fun makeRequest(input: Map<String, Any>?): ResponseEntity<out Map<*, *>> {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON

        val request = RequestEntity(input, requestHeaders, HttpMethod.POST, URI("/worlds"))

        return makeRequest(client, request, Map::class.java)
    }
}
