package uk.co.grahamcox.goworlds.service.users.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

internal class UserJdbcDaoTest : IntegrationTestBase() {
    /** The test subject */
    private lateinit var userJdbcDao: UserJdbcDao

    /** Set up the test subject */
    @BeforeEach
    fun setup() {
        userJdbcDao = UserJdbcDao(jdbcTemplate)
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
}