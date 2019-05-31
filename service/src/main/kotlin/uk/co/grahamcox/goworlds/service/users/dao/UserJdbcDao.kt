package uk.co.grahamcox.goworlds.service.users.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.model.Identity
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.UserRetriever
import uk.co.grahamcox.skl.select
import java.sql.ResultSet
import java.util.*

/**
 * JDBC based DAO for working with Users
 */
class UserJdbcDao(private val jdbcOperations: NamedParameterJdbcOperations) : UserRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(UserJdbcDao::class.java)
    }

    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user details
     */
    override fun getUserById(id: UserId): Model<UserId, UserData> {
        LOG.debug("Getting user with ID: {}", id)
        try {
            val query = select {
                val (users) = from("users")
                where {
                    eq(users["user_id"], bind(id.id))
                }
            }

            val user = jdbcOperations.queryForObject(query.sql, query.binds) { rs, _ ->
                parseUser(rs)
            }!!

            LOG.debug("Loaded user: {}", user)
            return user
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No user found with ID {}", id)
            throw UnknownUserException(id)
        }
    }

    /**
     * Parse the user that is represented by the current row in the given resultset
     * @param rs The result set to parse
     * @return the parsed user
     */
    private fun parseUser(rs: ResultSet) : Model<UserId, UserData> {
        return Model(
                identity = Identity(
                        id = UserId(rs.getUUID("user_id")),
                        version = UUID.fromString(rs.getString("version")),
                        created = rs.getInstant("created"),
                        updated = rs.getInstant("updated")
                ),
                data = UserData(
                        name = rs.getString("name"),
                        email = rs.getString("email"),
                        password = HashedPassword(rs.getString("password"))
                )
        )
    }
}
