package uk.co.grahamcox.goworlds.service.worlds.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import uk.co.grahamcox.goworlds.service.worlds.UnknownWorldException
import uk.co.grahamcox.goworlds.service.worlds.WorldId
import java.util.*

internal class WorldJdbcDaoTest : IntegrationTestBase() {
    /** The test subject */
    private lateinit var worldJdbcDao: WorldJdbcDao

    /** Set up the test subject */
    @BeforeEach
    fun setup() {
        worldJdbcDao = WorldJdbcDao(jdbcTemplate)
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
}
