package uk.co.grahamcox.goworlds.service.users.dao

import uk.co.grahamcox.goworlds.service.integration.database.Seeder
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import java.time.Instant
import java.util.*

/**
 * User to seed the database with
 */
data class UserSeed(
        val id: UUID = UUID.randomUUID(),
        val version: UUID = UUID.randomUUID(),
        val created: Instant = Instant.parse("2019-04-18T10:40:20Z"),
        val updated: Instant = Instant.parse("2019-05-18T10:40:20Z"),
        val name: String = "Graham",
        val email: String = "graham@grahamcox.co.uk",
        val password: String = "password"
) : Seeder {
    /** The hashed password */
    val hashedPassword = HashedPassword.hash(password).hash

    /** The SQL to seed with */
    override val seedSql = """INSERT INTO users(user_id, version, created, updated, name, email, password)
            VALUES(:id, :version, :created, :updated, :name, :email, :hashedPassword)"""
}
