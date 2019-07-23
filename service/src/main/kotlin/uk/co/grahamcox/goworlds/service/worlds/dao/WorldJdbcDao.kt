package uk.co.grahamcox.goworlds.service.worlds.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.database.queryForObject
import uk.co.grahamcox.goworlds.service.model.Identity
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.worlds.UnknownWorldException
import uk.co.grahamcox.goworlds.service.worlds.WorldData
import uk.co.grahamcox.goworlds.service.worlds.WorldId
import uk.co.grahamcox.goworlds.service.worlds.WorldService
import uk.co.grahamcox.skl.select
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