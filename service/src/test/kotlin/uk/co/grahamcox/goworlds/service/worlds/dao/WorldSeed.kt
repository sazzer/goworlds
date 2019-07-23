package uk.co.grahamcox.goworlds.service.worlds.dao

import uk.co.grahamcox.goworlds.service.integration.database.Seeder
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.ResponseType
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import java.net.URI
import java.time.Instant
import java.util.*

/**
 * World to seed the database with
 */
data class WorldSeed(
        val id: UUID = UUID.randomUUID(),
        val version: UUID = UUID.randomUUID(),
        val created: Instant = Instant.parse("2019-04-18T10:40:20Z"),
        val updated: Instant = Instant.parse("2019-05-18T10:40:20Z"),
        val name: String = "Test World",
        val ownerId: UUID,
        val description: String = "Test Description",
        val slug: String = "test_world"
) : Seeder {
    /** The SQL to seed with */
    override val seedSql = """INSERT INTO worlds(world_id, version, created, updated, name, owner_id, description, slug)
            VALUES(:id, :version, :created, :updated, :name, :ownerId, :description, :slug)"""
}
