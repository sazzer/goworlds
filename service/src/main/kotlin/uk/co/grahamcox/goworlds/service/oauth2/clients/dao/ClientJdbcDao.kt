package uk.co.grahamcox.goworlds.service.oauth2.clients.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import uk.co.grahamcox.goworlds.service.database.getInstant
import uk.co.grahamcox.goworlds.service.database.getList
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.model.Identity
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.clients.*
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UserId
import java.net.URI
import java.sql.ResultSet
import java.util.*

/**
 * JDBC based DAO for working with Clients
 */
class ClientJdbcDao(private val jdbcOperations: NamedParameterJdbcOperations) : ClientRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(ClientJdbcDao::class.java)
    }

    /**
     * Get the client with the given ID
     * @param id The ID of the client
     * @return the client details
     */
    override fun getClientById(id: ClientId): Model<ClientId, ClientData> {
        ClientJdbcDao.LOG.debug("Getting client with ID: {}", id)
        try {
            val client = jdbcOperations.queryForObject("SELECT * FROM oauth2_clients WHERE client_id = :clientId",
                    mapOf("clientId" to id.id)) { rs, _ ->
                parseClient(rs)
            }!!

            LOG.debug("Loaded client: {}", client)
            return client
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No client found with ID {}", id)
            throw UnknownClientException(id)
        }
    }

    /**
     * Parse the client that is represented by the current row in the given resultset
     * @param rs The result set to parse
     * @return the parsed client
     */
    private fun parseClient(rs: ResultSet) : Model<ClientId, ClientData> {
        return Model(
                identity = Identity(
                        id = ClientId(rs.getUUID("client_id")),
                        version = UUID.fromString(rs.getString("version")),
                        created = rs.getInstant("created"),
                        updated = rs.getInstant("updated")
                ),
                data = ClientData(
                        name = rs.getString("name"),
                        secret = HashedPassword(rs.getString("client_secret")),
                        owner = UserId(rs.getUUID("owner_id")),
                        redirectUris = rs.getList<String>("redirect_uris")
                                .map(::URI)
                                .toSet(),
                        responseTypes = rs.getList<String>("response_types")
                                .map(ResponseType::valueOf)
                                .toSet(),
                        grantTypes = rs.getList<String>("grant_types")
                                .map(GrantType::valueOf)
                                .toSet()
                )
        )
    }
}
