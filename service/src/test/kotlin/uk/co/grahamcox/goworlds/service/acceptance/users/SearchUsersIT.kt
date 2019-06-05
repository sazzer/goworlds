package uk.co.grahamcox.goworlds.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.time.Instant
import java.util.*

/**
 * Integration tests for searching users
 */
class SearchUsersIT : IntegrationTestBase() {
    @TestFactory
    fun searchNoUsers(): List<DynamicTest> {
        return listOf(
                // No parameters
                "/users" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Parameters matching defaults
                "/users?offset=0" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?count=10" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?offset=0&count=10" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?count=10&offset=0" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Filters
                "/users?name=unknown" to """{
                    "self": "/users/?name=unknown&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?email=unknown" to """{
                    "self": "/users/?email=unknown&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?name=other&email=unknown" to """{
                    "self": "/users/?name=other&email=unknown&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?email=other&name=unknown" to """{
                    "self": "/users/?name=unknown&email=other&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",

                // Sorts
                "/users?sort=name" to """{
                    "self": "/users/?sort=name&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?sort=+created" to """{
                    "self": "/users/?sort=%20created&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?sort=-updated" to """{
                    "self": "/users/?sort=-updated&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?sort=name,+created,-updated" to """{
                    "self": "/users/?sort=name,%20created,-updated&offset=0&count=10",
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
    fun searchUsers(): List<DynamicTest> {
        // Sort by name - 1, 2, 3
        // Sort by created - 2, 1, 3
        // Sort by updated - 3, 1, 2
        // Sort by id - 1, 3, 2
        val user1 = seed(UserSeed(
                id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                name = "ABC",
                email = "abc@example.com",
                created = Instant.parse("2019-03-01T12:34:56Z"),
                updated = Instant.parse("2019-03-01T12:34:56Z")
        ))
        val user2 = seed(UserSeed(
                id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                name = "def",
                email = "def@example.com",
                created = Instant.parse("2019-02-01T12:34:56Z"),
                updated = Instant.parse("2019-04-01T12:34:56Z")
        ))
        val user3 = seed(UserSeed(
                id = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                name = "GHI",
                email = "ghi@example.com",
                created = Instant.parse("2019-04-01T12:34:56Z"),
                updated = Instant.parse("2019-02-01T12:34:56Z")
        ))

        val user1Json = """{
                            "self": "/users/${user1.id}",
                            "created": "${user1.created}",
                            "updated": "${user1.updated}",
                            "name": "${user1.name}",
                            "email": "${user1.email}"
                        }"""
        val user2Json = """{
                            "self": "/users/${user2.id}",
                            "created": "${user2.created}",
                            "updated": "${user2.updated}",
                            "name": "${user2.name}",
                            "email": "${user2.email}"
                        }"""
        val user3Json = """{
                            "self": "/users/${user3.id}",
                            "created": "${user3.created}",
                            "updated": "${user3.updated}",
                            "name": "${user3.name}",
                            "email": "${user3.email}"
                        }"""
        return listOf(
                // No parameters
                "/users" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [$user1Json, $user3Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",

                // Parameters matching defaults
                "/users?offset=0" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [$user1Json, $user3Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?count=10" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [$user1Json, $user3Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?offset=0&count=10" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [$user1Json, $user3Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?count=10&offset=0" to """{
                    "self": "/users/?offset=0&count=10",
                    "entries": [$user1Json, $user3Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",

                // Adjusting Offset and Count
                "/users?offset=1" to """{
                    "self": "/users/?offset=1&count=10",
                    "previous": "/users/?offset=0&count=10",
                    "entries": [$user3Json, $user2Json],
                    "offset": 1,
                    "total": 3
                    }""",
                "/users?count=1" to """{
                    "self": "/users/?offset=0&count=1",
                    "next": "/users/?offset=1&count=1",
                    "entries": [$user1Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?offset=1&count=1" to """{
                    "self": "/users/?offset=1&count=1",
                    "previous": "/users/?offset=0&count=1",
                    "next": "/users/?offset=2&count=1",
                    "entries": [$user3Json],
                    "offset": 1,
                    "total": 3
                    }""",
                "/users?count=2&offset=2" to """{
                    "self": "/users/?offset=2&count=2",
                    "previous": "/users/?offset=0&count=2",
                    "entries": [$user2Json],
                    "offset": 2,
                    "total": 3
                    }""",
                
                // Filtering
                "/users?name=ABC" to """{
                    "self": "/users/?name=ABC&offset=0&count=10",
                    "entries": [$user1Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/users?name=abc" to """{
                    "self": "/users/?name=abc&offset=0&count=10",
                    "entries": [$user1Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/users?email=ghi@example.com" to """{
                    "self": "/users/?email=ghi@example.com&offset=0&count=10",
                    "entries": [$user3Json],
                    "offset": 0,
                    "total": 1
                    }""",
                "/users?name=def&email=ghi@example.com" to """{
                    "self": "/users/?name=def&email=ghi@example.com&offset=0&count=10",
                    "entries": [],
                    "offset": 0,
                    "total": 0
                    }""",
                "/users?email=ghi@example.com&name=ghi" to """{
                    "self": "/users/?name=ghi&email=ghi@example.com&offset=0&count=10",
                    "entries": [$user3Json],
                    "offset": 0,
                    "total": 1
                    }""",

                // Sorting
                "/users?sort=name" to """{
                    "self": "/users/?sort=name&offset=0&count=10",
                    "entries": [$user1Json, $user2Json, $user3Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?sort=+name" to """{
                    "self": "/users/?sort=%20name&offset=0&count=10",
                    "entries": [$user1Json, $user2Json, $user3Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?sort=-name" to """{
                    "self": "/users/?sort=-name&offset=0&count=10",
                    "entries": [$user3Json, $user2Json, $user1Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?sort=updated" to """{
                    "self": "/users/?sort=updated&offset=0&count=10",
                    "entries": [$user3Json, $user1Json, $user2Json],
                    "offset": 0,
                    "total": 3
                    }""",
                "/users?sort=name,updated" to """{
                    "self": "/users/?sort=name,updated&offset=0&count=10",
                    "entries": [$user1Json, $user2Json, $user3Json],
                    "offset": 0,
                    "total": 3
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
}
