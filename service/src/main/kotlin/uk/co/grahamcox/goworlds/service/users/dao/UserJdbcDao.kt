package uk.co.grahamcox.goworlds.service.users.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.database.queryForObject
import uk.co.grahamcox.goworlds.service.database.queryForPage
import uk.co.grahamcox.goworlds.service.model.*
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.*
import uk.co.grahamcox.skl.*
import java.sql.ResultSet
import java.time.Clock
import java.util.*

/**
 * JDBC based DAO for working with Users
 */
class UserJdbcDao(
        private val jdbcOperations: NamedParameterJdbcOperations,
        private val clock: Clock
) : UserService {
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

            val user = jdbcOperations.queryForObject(query, this::parseUser)

            LOG.debug("Loaded user: {}", user)
            return user
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No user found with ID {}", id)
            throw UnknownUserException(id)
        }
    }

    /**
     * Search for users in the system
     * @param filters Filters to apply when searching
     * @param sorts Sorts to apply for the returned users
     * @param offset The offset of the results to return
     * @param count The count of results to return
     * @return the matching users
     */
    override fun searchUsers(filters: UserSearchFilters, sorts: List<Sort<UserSort>>, offset: Long, count: Long): Page<Model<UserId, UserData>> {
        val selectBuilder: SelectBuilder.() -> Unit = {
            // Tables to select from
            val (users) = from("users")

            // Where clause to apply
            where {
                filters.apply {
                    name?.let { eq(upper(users["name"]), upper(bind(it))) }
                    email?.let { eq(upper(users["email"]), upper(bind(it))) }
                }
            }

            // Sorts to apply
            sorts.map { sort ->
                val sortField = when(sort.field) {
                    UserSort.NAME -> upper(users["name"])
                    UserSort.CREATED -> users["created"]
                    UserSort.UPDATED -> users["updated"]
                }
                val direction = when(sort.direction) {
                    SortDirection.ASCENDING -> SortOrder.ASCENDING
                    SortDirection.DESCENDING -> SortOrder.DESCENDING
                }

                OrderBy(sortField, direction)
            }.forEach {
                orderBy(it)
            }

            orderBy(users["user_id"], SortOrder.ASCENDING)
        }

        return jdbcOperations.queryForPage(selectBuilder, offset, count, this::parseUser)
    }

    /**
     * Create a new user
     * @param data The data for the user
     * @return the created user
     */
    override fun createUser(data: UserData): Model<UserId, UserData> {
        LOG.debug("Creating user with details: {}", data)

        val now = Date.from(clock.instant())

        val query = insert("users") {
            set("user_id", bind(UUID.randomUUID()))
            set("version", bind(UUID.randomUUID()))
            set("created", bind(now))
            set("updated", bind(now))

            set("name", bind(data.name))
            set("email", bind(data.email))
            set("password", bind(data.password.hash))

            returnAll()
        }

        val user = try {
            jdbcOperations.queryForObject(query, this::parseUser)
        } catch (e: DuplicateKeyException) {
            // This can only mean that the email address is a duplicate, because nothing else in the table is UNIQUE
            LOG.info("Duplicate Key error creating a new user", e)
            throw DuplicateEmailException(data.email)
        }

        LOG.debug("Created user: {}", user)
        return user
    }

    /**
     * Update the user to the given data
     * @param userId The ID of the user
     * @param data The new data
     * @return the newly updated user
     */
    private fun updateUser(userId: UserId, data: UserData): Model<UserId, UserData> {
        LOG.debug("Updating user {} with details: {}", userId, data)

        val now = Date.from(clock.instant())

        val query = update("users") {
            set("version", bind(UUID.randomUUID()))
            set("updated", bind(now))

            set("name", bind(data.name))
            set("email", bind(data.email))
            set("password", bind(data.password.hash))

            where {
                eq(field("user_id"), bind(userId.id))
            }

            returnAll()
        }

        val user = try {
            jdbcOperations.queryForObject(query, this::parseUser)
        } catch (e: DuplicateKeyException) {
            // This can only mean that the email address is a duplicate, because nothing else in the table is UNIQUE
            LOG.info("Duplicate Key error creating a new user", e)
            throw DuplicateEmailException(data.email)
        }

        LOG.debug("Updated user: {}", user)
        return user
    }

    /**
     * Update the user with the given lambda
     * @param userId The ID of the user
     * @param modifier The means to mutate the user
     * @return the newly updated user
     */
    override fun updateUser(userId: UserId, modifier: (Model<UserId, UserData>) -> UserData): Model<UserId, UserData> {
        val currentUser = getUserById(userId)

        val newData = modifier(currentUser)

        return updateUser(userId, newData)
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
