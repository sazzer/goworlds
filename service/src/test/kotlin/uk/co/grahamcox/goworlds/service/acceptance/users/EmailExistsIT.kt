package uk.co.grahamcox.goworlds.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed

/**
 * Integration tests for checking if an email exists
 */
class EmailExistsIT : IntegrationTestBase() {
    @Test
    fun getUnknownUser() {
        val response = restTemplate.getForEntity("/emails/unknown@example.com", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { assertJson("""{
                    "exists": false
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getKnownUser() {
        seed(UserSeed(email = "known@example.co.uk"))
        val response = restTemplate.getForEntity("/emails/known@example.co.uk", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { assertJson("""{
                    "exists": true
                }""".trimMargin(), response.body!!) }
        )
    }
}
