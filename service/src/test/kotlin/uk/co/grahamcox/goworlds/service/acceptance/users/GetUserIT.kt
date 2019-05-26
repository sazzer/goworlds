package uk.co.grahamcox.goworlds.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.util.*

/**
 * Integration tests for getting a user from the server
 */
class GetUserIT : IntegrationTestBase() {
    @Test
    fun getUnknownUser() {
        val response = restTemplate.getForEntity("/users/${UUID.randomUUID()}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:users/problems/unknown-user",
                    "title": "The requested user could not be found",
                    "status": 404
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getUnknownUser_InvalidId() {
        val response = restTemplate.getForEntity("/users/invalid", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:users/problems/unknown-user",
                    "title": "The requested user could not be found",
                    "status": 404
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getUser() {
        val user = seed(UserSeed())
        val response = restTemplate.getForEntity("/users/${user.id}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { assertJson("""{
                    "self": "/users/${user.id}",
                    "created": "${user.created}",
                    "updated": "${user.updated}",
                    "name": "${user.name}",
                    "email": "${user.email}"
                }""".trimMargin(), response.body!!) }
        )
    }
}
