package uk.co.grahamcox.goworlds.service.oauth2.clients.dao

import uk.co.grahamcox.goworlds.service.integration.database.Seeder
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.ResponseType
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import java.net.URI
import java.time.Instant
import java.util.*

/**
 * Client to seed the database with
 */
data class ClientSeed(
        val id: UUID = UUID.randomUUID(),
        val version: UUID = UUID.randomUUID(),
        val created: Instant = Instant.parse("2019-04-18T10:40:20Z"),
        val updated: Instant = Instant.parse("2019-05-18T10:40:20Z"),
        val name: String = "Test Client",
        val secret: String = "Test Secret",
        val ownerId: UUID,
        val redirectUris: Set<URI> = setOf(),
        val responseTypes: Set<ResponseType> = setOf(),
        val grantTypes: Set<GrantType> = setOf()
) : Seeder {
    /** The hashed password */
    val clientSecret = HashedPassword.hash(secret).hash

    /** The Redirect URIs as an array */
    val redirectUrisArray: Array<String> = redirectUris.map(URI::toString).toTypedArray()

    /** The Response Types as an array */
    val responseTypesArray: Array<String> = responseTypes.map(ResponseType::name).toTypedArray()

    /** The Grant Types as an array */
    val grantTypesArray: Array<String> = grantTypes.map(GrantType::name).toTypedArray()

    /** The SQL to seed with */
    override val seedSql = """INSERT INTO oauth2_clients(client_id, version, created, updated, name, client_secret, owner_id, redirect_uris, response_types, grant_types)
            VALUES(:id, :version, :created, :updated, :name, :clientSecret, :ownerId, :redirectUrisArray, :responseTypesArray, :grantTypesArray)"""
}
