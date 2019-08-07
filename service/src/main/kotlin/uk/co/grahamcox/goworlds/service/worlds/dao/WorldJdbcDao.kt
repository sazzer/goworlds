package uk.co.grahamcox.goworlds.service.worlds.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.database.queryForObject
import uk.co.grahamcox.goworlds.service.database.queryForPage
import uk.co.grahamcox.goworlds.service.model.*
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.worlds.*
import uk.co.grahamcox.skl.*
import uk.co.grahamcox.skl.postgres.*
import java.sql.ResultSet
import java.time.Clock
import java.util.*

/**
 * JDBC based DAO for working with Worlds
 */
@Transactional
open class WorldJdbcDao(
        private val jdbcOperations: NamedParameterJdbcOperations,
        private val clock: Clock
) : WorldService {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(WorldJdbcDao::class.java)
    }

    /**
     * Get the world with the given ID
     * @param id The ID of the world
     * @return the world details
     */
    override fun getById(id: WorldId): Model<WorldId, WorldData> {
        LOG.debug("Getting world with ID: {}", id)
        try {
            val query = select {
                val (worlds) = from("worlds")
                where {
                    eq(worlds["world_id"], bind(id.id))
                }
            }

            val world = jdbcOperations.queryForObject(query, this::parseWorld)

            LOG.debug("Loaded world: {}", world)
            return world
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No world found with ID {}", id)
            throw UnknownWorldException(id)
        }
    }

    /**
     * Get the world with the given URL Slug
     * @param slug The URL Slug of the World
     * @return the World details
     */
    override fun getBySlug(slug: String): Model<WorldId, WorldData> {
        LOG.debug("Getting world with Slug: {}", slug)
        try {
            val query = select {
                val (worlds) = from("worlds")
                where {
                    eq(worlds["slug"], bind(slug))
                }
            }

            val world = jdbcOperations.queryForObject(query, this::parseWorld)

            LOG.debug("Loaded world: {}", world)
            return world
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No world found with Slug {}", slug)
            throw UnknownWorldException(slug)
        }
    }

    /**
     * Search for worlds in the system
     * @param filters Filters to apply when searching
     * @param sorts Sorts to apply for the returned worlds
     * @param offset The offset of the results to return
     * @param count The count of results to return
     * @return the matching worlds
     */
    override fun search(filters: WorldSearchFilters, sorts: List<Sort<WorldSort>>, offset: Long, count: Long): Page<Model<WorldId, WorldData>> {
        val selectBuilder: SelectBuilder.() -> Unit = {
            // Tables to select from
            val (worlds) = from("worlds")
            selecting(worlds["*"])

            val usersTable = if (filters.owner != null || sorts.any { it.field == WorldSort.OWNER }) {
                // We're doing something with the owner of the world, so join across
                val (users) = from("users")
                where {
                    eq(worlds["owner_id"], users["user_id"])
                }
                users
            } else {
                null
            }

            // Build the Keyword Search details in case we need them
            val tsVector = Concatenate(
                    setWeight(worlds["name"], "A", true),
                    setWeight(worlds["description"], "B", true)
            )
            val tsQuery = toTsQuery(bind(filters.keyword), TsQueryForm.WEB_SEARCH)

            // Where clause to apply
            where {
                filters.apply {
                    name?.let { eq(upper(worlds["name"]), upper(bind(it))) }
                    owner?.let { eq(usersTable!!["user_id"], bind(it)) }
                    keyword?.let { matches(tsVector, tsQuery) }
                }
            }

            // Sorts to apply
            sorts.mapNotNull { sort ->
                val sortField = when (sort.field) {
                    WorldSort.NAME -> upper(worlds["name"])
                    WorldSort.CREATED -> worlds["created"]
                    WorldSort.UPDATED -> worlds["updated"]
                    WorldSort.OWNER -> upper(usersTable!!["name"])
                    WorldSort.RELEVANCE -> {
                        filters.keyword?.let { tsRankCd(tsVector, tsQuery) }
                    }

                }
                if (sortField != null) {
                    val direction = when (sort.direction) {
                        SortDirection.ASCENDING -> SortOrder.ASCENDING
                        SortDirection.DESCENDING -> SortOrder.DESCENDING
                    }

                    OrderBy(sortField, direction)
                } else {
                    null
                }
            }.forEach { orderBy(it) }

            orderBy(worlds["world_id"], SortOrder.ASCENDING)
        }

        return jdbcOperations.queryForPage(selectBuilder, offset, count, this::parseWorld)
    }

    /**
     * Create a new record
     * @param data The data for the record
     * @return the created data
     */
    override fun create(data: WorldData): Model<WorldId, WorldData> {
        LOG.debug("Creating world with details: {}", data)

        val now = Date.from(clock.instant())

        val query = insert("worlds") {
            set("world_id", bind(UUID.randomUUID()))
            set("version", bind(UUID.randomUUID()))
            set("created", bind(now))
            set("updated", bind(now))

            set("name", bind(data.name))
            set("owner_id", bind(data.owner.id))
            set("description", bind(data.description))
            set("slug", bind(data.slug))

            returnAll()
        }

        val world = try {
            jdbcOperations.queryForObject(query, this::parseWorld)
        } catch (e: DuplicateKeyException) {
            LOG.warn("Duplicate Key Exception creating new world {}", data, e)
            throw DuplicateSlugException(data.slug)
        } catch (e: DataIntegrityViolationException) {
            LOG.warn("Data Integrity Violation Exception creating new world {}", data, e)
            throw UnknownUserException(data.owner)
        }

        LOG.debug("Created world: {}", world)
        return world
    }

    /**
     * Update the world with the given lambda
     * @param worldId The ID of the world
     * @param modifier The means to mutate the world
     * @return the newly updated world
     */
    override fun update(worldId: WorldId, modifier: (Model<WorldId, WorldData>) -> WorldData): Model<WorldId, WorldData> {
        val currentWorld = getById(worldId)

        val newData = modifier(currentWorld)

        return updateWorld(worldId, newData)
    }

    /**
     * Update the world to the given data
     * @param worldId The ID of the world
     * @param data The new data
     * @return the newly updated world
     */
    private fun updateWorld(worldId: WorldId, data: WorldData): Model<WorldId, WorldData> {
        LOG.debug("Updating world {} with details: {}", worldId, data)

        val now = Date.from(clock.instant())

        val query = update("worlds") {
            set("version", bind(UUID.randomUUID()))
            set("updated", bind(now))

            set("name", bind(data.name))
            set("owner_id", bind(data.owner.id))
            set("description", bind(data.description))
            set("slug", bind(data.slug))

            where {
                eq(field("world_id"), bind(worldId.id))
            }

            returnAll()
        }

        val world = try {
            jdbcOperations.queryForObject(query, this::parseWorld)
        } catch (e: DuplicateKeyException) {
            LOG.warn("Duplicate Key Exception creating new world {}", data, e)
            throw DuplicateSlugException(data.slug)
        } catch (e: DataIntegrityViolationException) {
            LOG.warn("Data Integrity Violation Exception creating new world {}", data, e)
            throw UnknownUserException(data.owner)
        }

        LOG.debug("Updated world: {}", world)
        return world
    }
    
    /**
     * Parse the world that is represented by the current row in the given resultset
     * @param rs The result set to parse
     * @return the parsed world
     */
    private fun parseWorld(rs: ResultSet) : Model<WorldId, WorldData> {
        return Model(
                identity = Identity(
                        id = WorldId(rs.getUUID("world_id")),
                        version = rs.getUUID("version"),
                        created = rs.getInstant("created"),
                        updated = rs.getInstant("updated")
                ),
                data = WorldData(
                        name = rs.getString("name"),
                        owner = UserId((rs.getUUID("owner_id"))),
                        description = rs.getString("description"),
                        slug = rs.getString("slug")
                )
        )
    }

}