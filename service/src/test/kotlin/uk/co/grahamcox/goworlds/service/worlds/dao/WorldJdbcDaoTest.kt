package uk.co.grahamcox.goworlds.service.worlds.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.model.Sort
import uk.co.grahamcox.goworlds.service.model.SortDirection
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

internal class WorldJdbcDaoTest : IntegrationTestBase() {
    companion object {
        /** The "current" time */
        private val NOW = Instant.parse("2019-06-06T20:28:00Z")
    }

    /** The test subject */
    private lateinit var worldJdbcDao: WorldJdbcDao

    /** Set up the test subject */
    @BeforeEach
    fun setup() {
        worldJdbcDao = WorldJdbcDao(jdbcTemplate, Clock.fixed(NOW, ZoneId.of("UTC")))
    }

    @Test
    fun getUnknownWorldById() {
        val worldId = WorldId(UUID.randomUUID())
        val exception = Assertions.assertThrows(UnknownWorldException::class.java) {
            worldJdbcDao.getById(worldId)
        }

        Assertions.assertEquals(worldId, exception.id)
    }

    @Test
    fun getUnknownWorldBySlug() {
        val exception = Assertions.assertThrows(UnknownWorldException::class.java) {
            worldJdbcDao.getBySlug("unknown")
        }

        Assertions.assertEquals("unknown", exception.id)
    }

    @Test
    fun getWorldById() {
        val seededUser = seed(UserSeed())
        val seededWorld = seed(WorldSeed(ownerId = seededUser.id))

        val worldId = WorldId(seededWorld.id)
        val world = worldJdbcDao.getById(worldId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(worldId, world.identity.id) },
                Executable { Assertions.assertEquals(seededWorld.version, world.identity.version) },
                Executable { Assertions.assertEquals(seededWorld.created, world.identity.created) },
                Executable { Assertions.assertEquals(seededWorld.updated, world.identity.updated) },

                Executable { Assertions.assertEquals(seededWorld.name, world.data.name) },
                Executable { Assertions.assertEquals(UserId(seededUser.id), world.data.owner) },
                Executable { Assertions.assertEquals(seededWorld.description, world.data.description) },
                Executable { Assertions.assertEquals(seededWorld.slug, world.data.slug) }
        )
    }

    @Test
    fun getWorldBySlug() {
        val seededUser = seed(UserSeed())
        val seededWorld = seed(WorldSeed(ownerId = seededUser.id, slug = "test_client"))

        val world = worldJdbcDao.getBySlug("test_client")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(WorldId(seededWorld.id), world.identity.id) },
                Executable { Assertions.assertEquals(seededWorld.version, world.identity.version) },
                Executable { Assertions.assertEquals(seededWorld.created, world.identity.created) },
                Executable { Assertions.assertEquals(seededWorld.updated, world.identity.updated) },

                Executable { Assertions.assertEquals(seededWorld.name, world.data.name) },
                Executable { Assertions.assertEquals(UserId(seededUser.id), world.data.owner) },
                Executable { Assertions.assertEquals(seededWorld.description, world.data.description) },
                Executable { Assertions.assertEquals(seededWorld.slug, world.data.slug) }
        )
    }

    @TestFactory
    fun searchNoWorlds(): List<DynamicTest> {
        data class Test(
                val name: String,
                val filters: WorldSearchFilters = WorldSearchFilters(),
                val sorts: List<Sort<WorldSort>> = emptyList(),
                val offset: Long = 0,
                val count: Long = 10
        )

        return listOf(
                Test("Default values"),
                Test(
                        name = "Everything",
                        filters = WorldSearchFilters(name = "Graham", owner = UUID.randomUUID()),
                        sorts = listOf(
                                Sort(WorldSort.NAME, SortDirection.DESCENDING),
                                Sort(WorldSort.UPDATED, SortDirection.ASCENDING),
                                Sort(WorldSort.OWNER, SortDirection.ASCENDING)
                        ),
                        offset = 10,
                        count = 10
                )
        ).map { test ->
            DynamicTest.dynamicTest(test.name) {
                val results = worldJdbcDao.search(test.filters, test.sorts, test.offset, test.count)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(0, results.entries.size) },
                        Executable { Assertions.assertEquals(0, results.total) },
                        Executable { Assertions.assertEquals(test.offset, results.offset) }
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

        data class Test(
                val name: String,
                val filters: WorldSearchFilters = WorldSearchFilters(),
                val sorts: List<Sort<WorldSort>> = emptyList(),
                val offset: Long = 0,
                val count: Long = 10,
                val results: List<WorldSeed>,
                val totalCount: Long = results.size.toLong()
        )

        return listOf(
                Test(
                        name = "Default values",
                        results = listOf(world1, world3, world2)
                ),

                Test(
                        name = "Filter - Name - No Matches",
                        filters = WorldSearchFilters(name = "Other"),
                        results = listOf()
                ),
                Test(
                        name = "Filter - Name - Matches",
                        filters = WorldSearchFilters(name = world2.name),
                        results = listOf(world2)
                ),

                Test(
                        name = "Filter - Owner - No Matches",
                        filters = WorldSearchFilters(owner = UUID.randomUUID()),
                        results = listOf()
                ),
                Test(
                        name = "Filter - Owner - Matches",
                        filters = WorldSearchFilters(owner = user1.id),
                        results = listOf(world1, world3)
                ),

                Test(
                        name = "Filter - Keyword - Dungeons & Dragons",
                        filters = WorldSearchFilters(keyword = "Dungeons & Dragons"),
                        results = listOf(world1, world3, world2)
                ),
                Test(
                        name = "Filter - Keyword - TSR",
                        filters = WorldSearchFilters(keyword = "TSR"),
                        results = listOf(world1, world3)
                ),

                Test(
                        name = "Sort - Name Ascending",
                        sorts = listOf(Sort(WorldSort.NAME, SortDirection.ASCENDING)),
                        results = listOf(world1, world2, world3)
                ),
                Test(
                        name = "Sort - Name Descending",
                        sorts = listOf(Sort(WorldSort.NAME, SortDirection.DESCENDING)),
                        results = listOf(world3, world2, world1)
                ),
                Test(
                        name = "Sort - Updated Ascending",
                        sorts = listOf(Sort(WorldSort.UPDATED, SortDirection.ASCENDING)),
                        results = listOf(world3, world1, world2)
                ),
                Test(
                        name = "Sort - Created Descending",
                        sorts = listOf(Sort(WorldSort.CREATED, SortDirection.DESCENDING)),
                        results = listOf(world3, world1, world2)
                ),
                Test(
                        name = "Sort - Owner Ascending",
                        sorts = listOf(Sort(WorldSort.OWNER, SortDirection.ASCENDING)),
                        results = listOf(world1, world3, world2)
                ),


                Test(
                        name = "Sort - Relevance - Dungeons & Dragons",
                        filters = WorldSearchFilters(keyword = "Dungeons & Dragons"),
                        sorts = listOf(Sort(WorldSort.RELEVANCE, SortDirection.ASCENDING)),
                        results = listOf(world1, world3, world2)
                ),
                Test(
                        name = "Sort - Relevance - TSR",
                        filters = WorldSearchFilters(keyword = "TSR"),
                        sorts = listOf(Sort(WorldSort.RELEVANCE, SortDirection.DESCENDING)),
                        results = listOf(world3, world1)
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
                        results = listOf(world1),
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
                        results = listOf(world2),
                        totalCount = 3
                )
        ).map { test ->
            DynamicTest.dynamicTest(test.name) {
                val results = worldJdbcDao.search(test.filters, test.sorts, test.offset, test.count)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.results.size, results.entries.size, "Number of results") },
                        Executable { Assertions.assertEquals(test.totalCount, results.total, "Total Count") },
                        Executable { Assertions.assertEquals(test.offset, results.offset, "Offset") },

                        Executable {
                            Assertions.assertEquals(
                                    test.results.map(WorldSeed::id).map(::WorldId),
                                    results.entries.map { it.identity.id },
                                    "Returned IDs"
                            )
                        }
                )
            }
        }
    }

    @Test
    fun createNewWorld() {
        val seededUser = seed(UserSeed())
        val world = worldJdbcDao.create(WorldData(
                name = "Test World",
                description = "This is a test world",
                slug = "test-world",
                owner = UserId(seededUser.id)
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(NOW, world.identity.created) },
                Executable { Assertions.assertEquals(NOW, world.identity.updated) },

                Executable { Assertions.assertEquals("Test World", world.data.name) },
                Executable { Assertions.assertEquals("This is a test world", world.data.description) },
                Executable { Assertions.assertEquals("test-world", world.data.slug) },
                Executable { Assertions.assertEquals(seededUser.id, world.data.owner.id) }
        )

        val loaded = worldJdbcDao.getById(world.identity.id)

        Assertions.assertEquals(loaded, world)
    }

    @Test
    fun createNewWorldNoOwner() {
        val owner = UserId(UUID.randomUUID())

        val exception = Assertions.assertThrows(UnknownUserException::class.java) {
            worldJdbcDao.create(WorldData(
                    name = "Test World",
                    description = "This is a test world",
                    slug = "test-world",
                    owner = owner
            ))
        }

        Assertions.assertEquals(owner, exception.id)
    }

    @Test
    fun createNewWorldDuplicateSlug() {
        val seededUser = seed(UserSeed())
        seed(WorldSeed(ownerId = seededUser.id, slug = "test-world"))

        val exception = Assertions.assertThrows(DuplicateSlugException::class.java) {
            worldJdbcDao.create(WorldData(
                    name = "Test World",
                    description = "This is a test world",
                    slug = "test-world",
                    owner = UserId(seededUser.id)
            ))
        }

        Assertions.assertEquals("test-world", exception.slug)
    }

}
