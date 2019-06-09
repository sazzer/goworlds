package uk.co.grahamcox.goworlds.service.users.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.model.Sort
import uk.co.grahamcox.goworlds.service.model.SortDirection
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

internal class UserJdbcDaoTest : IntegrationTestBase() {
    companion object {
        /** The "current" time */
        private val NOW = Instant.parse("2019-06-06T20:28:00Z")
    }

    /** The test subject */
    private lateinit var userJdbcDao: UserJdbcDao

    /** Set up the test subject */
    @BeforeEach
    fun setup() {
        userJdbcDao = UserJdbcDao(jdbcTemplate, Clock.fixed(NOW, ZoneId.of("UTC")))
    }

    @Test
    fun getUnknownUserById() {
        val userId = UserId(UUID.randomUUID())
        val exception = Assertions.assertThrows(UnknownUserException::class.java) {
            userJdbcDao.getUserById(userId)
        }

        Assertions.assertEquals(userId, exception.id)
    }

    @Test
    fun getKnownUserById() {
        val seededUser = seed(UserSeed())

        val userId = UserId(seededUser.id)
        val user = userJdbcDao.getUserById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(seededUser.version, user.identity.version) },
                Executable { Assertions.assertEquals(seededUser.created, user.identity.created) },
                Executable { Assertions.assertEquals(seededUser.updated, user.identity.updated) },

                Executable { Assertions.assertEquals(seededUser.name, user.data.name) },
                Executable { Assertions.assertEquals(seededUser.email, user.data.email) },
                Executable { Assertions.assertEquals(HashedPassword(seededUser.hashedPassword), user.data.password) }
        )
    }

    @TestFactory
    fun searchNoUsers(): List<DynamicTest> {
        data class Test(
                val name: String,
                val filters: UserSearchFilters = UserSearchFilters(),
                val sorts: List<Sort<UserSort>> = emptyList(),
                val offset: Long = 0,
                val count: Long = 10
        )

        return listOf(
                Test("Default values"),
                Test(
                        name = "Everything",
                        filters = UserSearchFilters(name = "Graham", email = "graham@grahamcox.co.uk"),
                        sorts = listOf(
                                Sort(UserSort.NAME, SortDirection.DESCENDING),
                                Sort(UserSort.UPDATED, SortDirection.ASCENDING)
                        ),
                        offset = 10,
                        count = 10
                )
        ).map { test ->
            DynamicTest.dynamicTest(test.name) {
                val results = userJdbcDao.searchUsers(test.filters, test.sorts, test.offset, test.count)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(0, results.entries.size) },
                        Executable { Assertions.assertEquals(0, results.total) },
                        Executable { Assertions.assertEquals(test.offset, results.offset) }
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


        data class Test(
                val name: String,
                val filters: UserSearchFilters = UserSearchFilters(),
                val sorts: List<Sort<UserSort>> = emptyList(),
                val offset: Long = 0,
                val count: Long = 10,
                val results: List<UserSeed>,
                val totalCount: Long = results.size.toLong()
        )

        return listOf(
                Test(
                        name = "Default values",
                        results = listOf(user1, user3, user2)
                ),

                Test(
                        name = "Filter - Name - No Matches",
                        filters = UserSearchFilters(name = "Other"),
                        results = listOf()
                ),
                Test(
                        name = "Filter - Email - No Matches",
                        filters = UserSearchFilters(email = "Other"),
                        results = listOf()
                ),
                Test(
                        name = "Filter - Name - Matches",
                        filters = UserSearchFilters(name = user2.name),
                        results = listOf(user2)
                ),
                Test(
                        name = "Filter - Email - Matches",
                        filters = UserSearchFilters(email = user3.email),
                        results = listOf(user3)
                ),

                Test(
                        name = "Sort - Name Ascending",
                        sorts = listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                        results = listOf(user1, user2, user3)
                ),
                Test(
                        name = "Sort - Name Descending",
                        sorts = listOf(Sort(UserSort.NAME, SortDirection.DESCENDING)),
                        results = listOf(user3, user2, user1)
                ),
                Test(
                        name = "Sort - Updated Ascending",
                        sorts = listOf(Sort(UserSort.UPDATED, SortDirection.ASCENDING)),
                        results = listOf(user3, user1, user2)
                ),
                Test(
                        name = "Sort - Created Descending",
                        sorts = listOf(Sort(UserSort.CREATED, SortDirection.DESCENDING)),
                        results = listOf(user3, user1, user2)
                ),

                Test(
                        name = "Limit 0",
                        count = 0,
                        results = listOf(),
                        totalCount = 3
                ),
                Test(
                        name = "Limit 1",
                        count = 1,
                        results = listOf(user1),
                        totalCount = 3
                ),
                Test(
                        name = "Offset off the end",
                        offset = 10,
                        results = listOf(),
                        totalCount = 3
                ),
                Test(
                        name = "Offset overlaps the end",
                        offset = 2,
                        results = listOf(user2),
                        totalCount = 3
                )
        ).map { test ->
            DynamicTest.dynamicTest(test.name) {
                val results = userJdbcDao.searchUsers(test.filters, test.sorts, test.offset, test.count)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.results.size, results.entries.size, "Number of results") },
                        Executable { Assertions.assertEquals(test.totalCount, results.total, "Total Count") },
                        Executable { Assertions.assertEquals(test.offset, results.offset, "Offset") },

                        Executable {
                            Assertions.assertEquals(
                                    test.results.map(UserSeed::id).map(::UserId),
                                    results.entries.map { it.identity.id },
                                    "Returned IDs"
                            )
                        }
                )
            }
        }
    }

    @Test
    fun createNewUser() {
        val hashedPassword = HashedPassword.hash("secret")

        val user = userJdbcDao.createUser(UserData(
                name = "Graham",
                email = "graham@grahamcox.co.uk",
                password = hashedPassword
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals("Graham", user.data.name) },
                Executable { Assertions.assertEquals("graham@grahamcox.co.uk", user.data.email) },
                Executable { Assertions.assertEquals(hashedPassword, user.data.password) }
        )

        val loaded = userJdbcDao.getUserById(user.identity.id)

        Assertions.assertEquals(loaded, user)
    }

    @Test
    fun createNewUserDuplicateEmail() {
        seed(UserSeed(
                email = "graham@grahamcox.co.uk"
        ))

        val e = Assertions.assertThrows(DuplicateEmailException::class.java) {
            userJdbcDao.createUser(UserData(
                    name = "Graham",
                    email = "graham@grahamcox.co.uk",
                    password = HashedPassword.hash("secret")
            ))
        }

        Assertions.assertEquals("graham@grahamcox.co.uk", e.email)
    }
    @Test
    fun updateUserSuccess() {
        val user = seed(UserSeed(
                password = "password"
        ))

        val updatedUser = userJdbcDao.updateUser(UserId(user.id)) { user ->
            user.data.copy(
                    name = "new name",
                    email = "new email"
            )
        }

        Assertions.assertAll(
                Executable { Assertions.assertEquals(updatedUser.identity.id, UserId(user.id)) },
                Executable { Assertions.assertNotEquals(updatedUser.identity.version, user.version) },
                Executable { Assertions.assertEquals(updatedUser.identity.created, user.created) },
                Executable { Assertions.assertNotEquals(updatedUser.identity.updated, user.updated) },

                Executable { Assertions.assertEquals(updatedUser.data.name, "new name") },
                Executable { Assertions.assertEquals(updatedUser.data.email, "new email") },
                // Unchanged
                Executable { Assertions.assertTrue(updatedUser.data.password.check("password")) }
        )
    }

    @Test
    fun updateUserReRetrieve() {
        val user = seed(UserSeed())

        val updatedUser = userJdbcDao.updateUser(UserId(user.id)) { user ->
            user.data.copy(
                    name = "new name",
                    email = "new email"
            )
        }

        val retrievedUser = userJdbcDao.getUserById(UserId(user.id))
        Assertions.assertEquals(updatedUser, retrievedUser)
    }

}
