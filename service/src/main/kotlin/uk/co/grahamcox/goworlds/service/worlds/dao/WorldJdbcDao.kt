package uk.co.grahamcox.goworlds.service.worlds.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.database.queryForObject
import uk.co.grahamcox.goworlds.service.database.queryForPage
import uk.co.grahamcox.goworlds.service.model.*
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.UserSearchFilters
import uk.co.grahamcox.goworlds.service.users.UserSort
import uk.co.grahamcox.goworlds.service.worlds.*
import uk.co.grahamcox.skl.*
import uk.co.grahamcox.skl.postgres.*
import java.sql.ResultSet

/**
 * JDBC based DAO for working with Worlds
 */
@Transactional
open class WorldJdbcDao(
        private val jdbcOperations: NamedParameterJdbcOperations
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