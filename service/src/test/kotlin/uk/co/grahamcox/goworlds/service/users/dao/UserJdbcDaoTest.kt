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
    /** The JDBC Template to use */
    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

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
        val userId = UserId(UUID.randomUUID())
        val version = UUID.randomUUID()
        val created = Instant.parse("2019-04-18T10:40:50Z")
        val updated = Instant.parse("2019-05-18T10:40:50Z")

        jdbcTemplate.update("""INSERT INTO users(user_id, version, created, updated, name, email, password)
            VALUES(:userId, :version, :created, :updated, :name, :email, :password)""",
                mapOf(
                        "userId" to userId.id,
                        "version" to version,
                        "created" to Date.from(created),
                        "updated" to Date.from(updated),
                        "name" to "Graham",
                        "email" to "graham@grahamcox.co.uk",
                        "password" to "passwordHash"
                ))

        val user = userJdbcDao.getUserById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(created, user.identity.created) },
                Executable { Assertions.assertEquals(updated, user.identity.updated) },

                Executable { Assertions.assertEquals("Graham", user.data.name) },
                Executable { Assertions.assertEquals("graham@grahamcox.co.uk", user.data.email) },
                Executable { Assertions.assertEquals(HashedPassword("passwordHash"), user.data.password) }
        )
    }
}
