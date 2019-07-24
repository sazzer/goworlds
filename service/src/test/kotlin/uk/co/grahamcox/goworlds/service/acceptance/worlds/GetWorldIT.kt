package uk.co.grahamcox.goworlds.service.acceptance.worlds

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.dao.WorldSeed
import java.util.*

/**
 * Integration tests for getting a world from the server
 */
class GetWorldIT : IntegrationTestBase() {
    @Test
    fun getUnknownWorld() {
        val response = restTemplate.getForEntity("/worlds/${UUID.randomUUID()}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:worlds/problems/unknown-world",
                    "title": "The requested world could not be found",
                    "status": 404
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getUnknownWorld_InvalidId() {
        val response = restTemplate.getForEntity("/worlds/invalid", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:worlds/problems/unknown-world",
                    "title": "The requested world could not be found",
                    "status": 404
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getWorldById() {
        val owner = seed(UserSeed())
        val world = seed(WorldSeed(ownerId = owner.id))
        val response = restTemplate.getForEntity("/worlds/${world.id}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertEquals(""""${world.version}"""", response.headers.eTag) },
                Executable { Assertions.assertEquals("""/worlds/${world.id}""", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) },
                Executable { assertJson("""{
                    "id": "${world.id}",
                    "created": "${world.created}",
                    "updated": "${world.updated}",
                    "name": "${world.name}",
                    "description": "${world.description}",
                    "slug": "${world.slug}",
                    "owner": "${world.ownerId}"
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getUnknownWorldBySlug() {
        val response = restTemplate.getForEntity("/worlds/by-slug/${UUID.randomUUID()}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                Executable { assertJson("""{
                    "type": "tag:goworlds,2019:worlds/problems/unknown-world",
                    "title": "The requested world could not be found",
                    "status": 404
                }""".trimMargin(), response.body!!) }
        )
    }

    @Test
    fun getWorldBySlug() {
        val owner = seed(UserSeed())
        val world = seed(WorldSeed(ownerId = owner.id))
        val response = restTemplate.getForEntity("/worlds/by-slug/${world.slug}", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                Executable { Assertions.assertEquals(""""${world.version}"""", response.headers.eTag) },
                Executable { Assertions.assertEquals("""/worlds/${world.id}""", response.headers.getFirst(HttpHeaders.CONTENT_LOCATION)) },
                Executable { assertJson("""{
                    "id": "${world.id}",
                    "created": "${world.created}",
                    "updated": "${world.updated}",
                    "name": "${world.name}",
                    "description": "${world.description}",
                    "slug": "${world.slug}",
                    "owner": "${world.ownerId}"
                }""".trimMargin(), response.body!!) }
        )
    }
}
