package uk.co.grahamcox.goworlds.service.oauth2.clients.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.ResponseType
import uk.co.grahamcox.goworlds.service.oauth2.clients.UnknownClientException
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.dao.UserSeed
import java.net.URI
import java.util.*

internal class ClientJdbcDaoTest : IntegrationTestBase() {
    /** The test subject */
    private lateinit var clientJdbcDao: ClientJdbcDao

    /** Set up the test subject */
    @BeforeEach
    fun setup() {
        clientJdbcDao = ClientJdbcDao(jdbcTemplate)
    }

    @Test
    fun getUnknownClientById() {
        val clientId = ClientId(UUID.randomUUID())
        val exception = Assertions.assertThrows(UnknownClientException::class.java) {
            clientJdbcDao.getClientById(clientId)
        }

        Assertions.assertEquals(clientId, exception.id)
    }

    @Test
    fun getSimpleClientById() {
        val seededUser = seed(UserSeed())
        val seededClient = seed(ClientSeed(ownerId = seededUser.id))

        val clientId = ClientId(seededClient.id)
        val client = clientJdbcDao.getClientById(clientId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(clientId, client.identity.id) },
                Executable { Assertions.assertEquals(seededClient.version, client.identity.version) },
                Executable { Assertions.assertEquals(seededClient.created, client.identity.created) },
                Executable { Assertions.assertEquals(seededClient.updated, client.identity.updated) },

                Executable { Assertions.assertEquals(seededClient.name, client.data.name) },
                Executable { Assertions.assertEquals(HashedPassword(seededClient.clientSecret), client.data.secret) },
                Executable { Assertions.assertEquals(UserId(seededUser.id), client.data.owner) },
                Executable { Assertions.assertEquals(emptySet<URI>(), client.data.redirectUris) },
                Executable { Assertions.assertEquals(emptySet<ResponseType>(), client.data.responseTypes) },
                Executable { Assertions.assertEquals(emptySet<GrantType>(), client.data.grantTypes) }
        )
    }

    @Test
    fun getPopulatedClientById() {
        val seededUser = seed(UserSeed())
        val seededClient = seed(ClientSeed(
                ownerId = seededUser.id,
                redirectUris = setOf(URI("http://localhost/redirect")),
                responseTypes = setOf(ResponseType.CODE, ResponseType.TOKEN),
                grantTypes = setOf(GrantType.AUTHORIZATION_CODE, GrantType.CLIENT_CREDENTIALS)
                ))

        val clientId = ClientId(seededClient.id)
        val client = clientJdbcDao.getClientById(clientId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(clientId, client.identity.id) },
                Executable { Assertions.assertEquals(seededClient.version, client.identity.version) },
                Executable { Assertions.assertEquals(seededClient.created, client.identity.created) },
                Executable { Assertions.assertEquals(seededClient.updated, client.identity.updated) },

                Executable { Assertions.assertEquals(seededClient.name, client.data.name) },
                Executable { Assertions.assertEquals(HashedPassword(seededClient.clientSecret), client.data.secret) },
                Executable { Assertions.assertEquals(UserId(seededUser.id), client.data.owner) },
                Executable { Assertions.assertEquals(seededClient.redirectUris, client.data.redirectUris) },
                Executable { Assertions.assertEquals(seededClient.responseTypes, client.data.responseTypes) },
                Executable { Assertions.assertEquals(seededClient.grantTypes, client.data.grantTypes) }
        )
    }
}
