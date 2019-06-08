package uk.co.grahamcox.goworlds.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable
import org.springframework.http.*
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.net.URI

/**
 * Acceptance tests for creating a user
 */
class CreateUserIT : IntegrationTestBase() {
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
                mapOf("email" to "graham@grahamcox.co.uk", "password" to "secret") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "name"
                }""",
                mapOf("name" to "Graham", "password" to "secret") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "email"
                }""",
                mapOf("name" to "Graham", "email" to "graham@grahamcox.co.uk") to """{
                    "type": "tag:goworlds,2019:problems/missing-request-field",
                    "title": "A request field was required but not present",
                    "status": 400,
                    "details": "password"
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
    fun createDuplicateEmail() {
        seed(UserSeed(email = "graham@grahamcox.co.uk"))

        val response = makeRequest(mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk",
                "password" to "secret"
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
    fun createSuccess() {
        val response = makeRequest(mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk",
                "password" to "secret"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertEquals("Graham", response.body?.get("name")) },
                Executable { Assertions.assertEquals("graham@grahamcox.co.uk", response.body?.get("email")) },
                Executable { Assertions.assertEquals(response.body?.get("self"), response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) }
        )
    }

    @Test
    fun createSuccessReRetrieve() {
        val created = makeRequest(mapOf(
                "name" to "Graham",
                "email" to "graham@grahamcox.co.uk",
                "password" to "secret"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, created.statusCode) },
                Executable { Assertions.assertTrue(created.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) }
        )

        val uri = URI(created.body!!["self"].toString())
        val retrieved = restTemplate.getForEntity(uri, Map::class.java)

        Assertions.assertEquals(retrieved.body, created.body)

    }

    /**
     * Actually make the request to the API
     */
    private fun makeRequest(input: Map<String, Any>?): ResponseEntity<Map<*, *>> {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(input, requestHeaders)

        return restTemplate.postForEntity("/users", request, Map::class.java)
    }
}
