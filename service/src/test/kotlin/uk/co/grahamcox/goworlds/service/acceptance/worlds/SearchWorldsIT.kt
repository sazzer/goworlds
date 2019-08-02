package uk.co.grahamcox.goworlds.service.acceptance.worlds

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.dao.WorldSeed
import java.time.Instant
import java.util.*

/**
 * Integration tests for searching worlds
 */
class SearchWorldsIT : IntegrationTestBase() {
    @TestFactory
    fun searchNoWorlds(): List<DynamicTest> {
        return listOf(
                // No parameters
                "/worlds" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Parameters matching defaults
                "/worlds?offset=0" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?count=10" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?offset=0&count=10" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?count=10&offset=0" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Filters
                "/worlds?name=unknown" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?owner=${UUID.randomUUID()}" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?name=other&owner=${UUID.randomUUID()}" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?owner=${UUID.randomUUID()}&name=unknown" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Sorts
                "/worlds?sort=name" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?sort=+created" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?sort=-updated" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?sort=name,+created,-updated" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }"""
        ).map { (url, expected) ->
            DynamicTest.dynamicTest(url) {
                val response = restTemplate.getForEntity(url, Map::class.java)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                        Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                        Executable { assertJson(expected, response.body!!) }
                )
            }
        }
    }

    @TestFactory
    fun searchWorlds(): List<DynamicTest> {
        val user1 = seed(UserSeed(
                name = "ABC",
                email = "ABC"
        ))
        val user2 = seed(UserSeed(
                name = "def",
                email = "def"
        ))

        // Sort by name - 1, 2, 3
        // Sort by created - 2, 1, 3
        // Sort by updated - 3, 1, 2
        // Sort by id - 1, 3, 2
        // Sort by owner - 1, 3, 2
        val world1 = seed(WorldSeed(
                id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                created = Instant.parse("2019-03-01T12:34:56Z"),
                updated = Instant.parse("2019-03-01T12:34:56Z"),
                name = "ABC",
                ownerId = user1.id,
                slug = "ABC",
                description = "Faerûn /feɪˈruːn/ is a fictional continent, the primary setting of the Dungeons & Dragons " +
                        "world of Forgotten Realms. It is described in detail in several editions of the Forgotten Realms " +
                        "Campaign Setting (first published in 1987 by TSR, Inc.) with the most recent being the 5th edition " +
                        "from Wizards of the Coast, and various locales and aspects are described in more depth in separate " +
                        "campaign setting books.[3] Around a hundred novels and several computer and video games use the Faerûn setting"
        ))
        val world2 = seed(WorldSeed(
                id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                created = Instant.parse("2019-02-01T12:34:56Z"),
                updated = Instant.parse("2019-04-01T12:34:56Z"),
                name = "def",
                ownerId = user2.id,
                slug = "def",
                description = "Eberron is a campaign setting for the Dungeons & Dragons (D&D) role-playing game, set in " +
                        "a period after a vast destructive war on the continent of Khorvaire. Eberron is designed to " +
                        "accommodate traditional D&D elements and races within a differently toned setting; Eberron combines " +
                        "a fantasy tone with pulp and dark adventure elements, and some non-traditional fantasy technologies " +
                        "such as trains, skyships, and mechanical beings which are all powered by magic."
        ))
        val world3 = seed(WorldSeed(
                id = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                created = Instant.parse("2019-04-01T12:34:56Z"),
                updated = Instant.parse("2019-02-01T12:34:56Z"),
                name = "GHI",
                ownerId = user1.id,
                slug = "GHI",
                description = "Dragonlance is a shared universe created by Laura and Tracy Hickman, and expanded by " +
                        "Tracy Hickman and Margaret Weis under the direction of TSR, Inc. into a series of fantasy novels. " +
                        "The Hickmans conceived Dragonlance while driving in their car on the way to TSR for a job interview. " +
                        "At TSR Tracy Hickman met Margaret Weis, his future writing partner, and they gathered a group " +
                        "of associates to play the Dungeons & Dragons role-playing game. The adventures during that game " +
                        "inspired a series of gaming modules, a series of novels, licensed products such as board games, " +
                        "and lead miniature figures."
        ))

        val world1Json = """{
                            "id": "${world1.id}",
                            "created": "${world1.created}",
                            "updated": "${world1.updated}",
                            "name": "${world1.name}",
                            "description": "${world1.description}",
                            "slug": "${world1.slug}",
                            "owner": "${world1.ownerId}"
                        }"""
        val world2Json = """{
                            "id": "${world2.id}",
                            "created": "${world2.created}",
                            "updated": "${world2.updated}",
                            "name": "${world2.name}",
                            "description": "${world2.description}",
                            "slug": "${world2.slug}",
                            "owner": "${world2.ownerId}"
                        }"""
        val world3Json = """{
                            "id": "${world3.id}",
                            "created": "${world3.created}",
                            "updated": "${world3.updated}",
                            "name": "${world3.name}",
                            "description": "${world3.description}",
                            "slug": "${world3.slug}",
                            "owner": "${world3.ownerId}"
                        }"""
        return listOf(
                // No parameters
                "/worlds" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",

                // Parameters matching defaults
                "/worlds?offset=0" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?count=10" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?offset=0&count=10" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?count=10&offset=0" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",

                // Adjusting Offset and Count
                "/worlds?offset=1" to """{
                    "entries": [$world3Json, $world2Json],
                    "offset": 1,
                    "total": 3
                    }""",
                "/worlds?count=1" to """{
                    "entries": [$world1Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?offset=1&count=1" to """{
                    "entries": [$world3Json],
                    "offset": 1,
                    "total": 3
                    }""",
                "/worlds?count=2&offset=2" to """{
                    "entries": [$world2Json],
                    "offset": 2,
                    "total": 3
                    }""",

                // Filtering
                "/worlds?name=ABC" to """{
                    "entries": [$world1Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/worlds?name=abc" to """{
                    "entries": [$world1Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/worlds?owner=${user1.id}" to """{
                    "entries": [$world1Json, $world3Json],
                    "offset": 0,
                    "total": 2
                    }""",
                "/worlds?name=def&owner=${user1.id}" to """{
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/worlds?owner=${user1.id}&name=ghi" to """{
                    "entries": [$world3Json],
                    "offset": 0,
                    "total": 1
                    }""",

                // Sorting
                "/worlds?sort=name" to """{
                    "entries": [$world1Json, $world2Json, $world3Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?sort=+name" to """{
                    "entries": [$world1Json, $world2Json, $world3Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?sort=-name" to """{
                    "entries": [$world3Json, $world2Json, $world1Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?sort=updated" to """{
                    "entries": [$world3Json, $world1Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?sort=name,updated" to """{
                    "entries": [$world1Json, $world2Json, $world3Json],
                    "offset": 0,
                    "total": 3
                    }""",

                // Keyword searching
                "/worlds?keyword=dungeons+and+dragons" to """{
                    "entries": [$world1Json, $world3Json, $world2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/worlds?keyword=skyship" to """{
                    "entries": [$world2Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/worlds?keyword=tsr&sort=-relevance" to """{
                    "entries": [$world3Json, $world1Json],
                    "offset": 0,
                    "total": 2
                    }"""
        ).map { (url, expected) ->
            DynamicTest.dynamicTest(url) {
                val response = restTemplate.getForEntity(url, Map::class.java)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                        Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },
                        Executable { assertJson(expected, response.body!!) }
                )
            }
        }
    }

    @TestFactory
    fun searchInvalidRequest(): List<DynamicTest> {
        return listOf(
                // Invalid Offset
                "/worlds?offset=a" to """{
                    "type": "tag:goworlds,2019:problems/invalid-offset",
                    "title": "The specified offset was invalid",
                    "status": 400
                    }""",
                "/worlds?offset=-1" to """{
                    "type": "tag:goworlds,2019:problems/invalid-offset",
                    "title": "The specified offset was invalid",
                    "status": 400
                    }""",
                "/worlds?offset=1.0" to """{
                    "type": "tag:goworlds,2019:problems/invalid-offset",
                    "title": "The specified offset was invalid",
                    "status": 400
                    }""",

                // Invalid Count
                "/worlds?count=a" to """{
                    "type": "tag:goworlds,2019:problems/invalid-count",
                    "title": "The specified count was invalid",
                    "status": 400
                    }""",
                "/worlds?count=-1" to """{
                    "type": "tag:goworlds,2019:problems/invalid-count",
                    "title": "The specified count was invalid",
                    "status": 400
                    }""",
                "/worlds?count=1.0" to """{
                    "type": "tag:goworlds,2019:problems/invalid-count",
                    "title": "The specified count was invalid",
                    "status": 400
                    }""",

                // Invalid Sorts
                "/worlds?sort=a" to """{
                    "type": "tag:goworlds,2019:problems/invalid-sort",
                    "title": "The specified sorts were invalid",
                    "status": 400,
                    "unknown-sorts": ["a"]
                    }""",
                "/worlds?sort=name,a" to """{
                    "type": "tag:goworlds,2019:problems/invalid-sort",
                    "title": "The specified sorts were invalid",
                    "status": 400,
                    "unknown-sorts": ["a"]
                    }""",
                "/worlds?sort=*a" to """{
                    "type": "tag:goworlds,2019:problems/invalid-sort",
                    "title": "The specified sorts were invalid",
                    "status": 400,
                    "unknown-sorts": ["*a"]
                    }""",
                "/worlds?sort=a,b,c" to """{
                    "type": "tag:goworlds,2019:problems/invalid-sort",
                    "title": "The specified sorts were invalid",
                    "status": 400,
                    "unknown-sorts": ["a", "b", "c"]
                    }"""
        ).map { (url, expected) ->
            DynamicTest.dynamicTest(url) {
                val response = restTemplate.getForEntity(url, Map::class.java)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                        Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)) },
                        Executable { assertJson(expected, response.body!!) }
                )
            }
        }
    }

}
