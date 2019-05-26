package uk.co.grahamcox.goworlds.service.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.*
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseCleaner
import uk.co.grahamcox.goworlds.service.integration.database.Seeder
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientSeed

/**
 * Base class for the Integration Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestConfig::class)
abstract class IntegrationTestBase {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(IntegrationTestBase::class.java)
    }

    /** The RestTemplate to test with */
    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    /** The Object Mapper to use  */
    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    /** The JDBC Template to use */
    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /**
     * Clean the database before each test
     */
    @BeforeEach
    fun cleanDatabase() {
        databaseCleaner.clean()
    }

    /**
     * Seed the database with some data
     * @param data The data to seed the database with
     * @return the data that was seeded
     */
    protected fun <T : Seeder> seed(data: T) : T {
        jdbcTemplate.update(data.seedSql, data.seedParams)
        LOG.debug("Seeded {}", data)
        return data
    }

    /**
     * Assert that the given JSON String matches the actual response
     */
    protected fun assertJson(expected: String, actual: Any) {
        val parsed = objectMapper.readValue(expected, actual.javaClass)
        Assertions.assertEquals(parsed, actual)
    }
    /**
     * Make an authenticated request to the service
     * @param client The client to authenticate as
     * @param request The request to process
     * @return the response
     */
    fun <T> makeRequest(token: String, request: RequestEntity<*>, response: Class<T>): ResponseEntity<out T> {
        val newRequestHeaders = LinkedMultiValueMap(request.headers)
        newRequestHeaders[HttpHeaders.AUTHORIZATION] = "Bearer $token"

        val newRequest = RequestEntity(request.body, newRequestHeaders, request.method, request.url)
        return restTemplate.exchange(newRequest, response)
    }

    /**
     * Make an authenticated request to the service
     * @param client The client to authenticate as
     * @param request The request to process
     * @return the response
     */
    fun <T> makeRequest(client: ClientSeed, request: RequestEntity<*>, response: Class<T>)
        = makeRequest(getTokenForClient(client), request, response)

    /**
     * Get an Access Token that works for the given client
     * @param client The client to authenticate as
     * @return the token
     */
    private fun getTokenForClient(client: ClientSeed): String {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        requestHeaders.setBasicAuth(client.id.toString(), client.secret)

        val params = mapOf("grant_type" to listOf("client_credentials"))

        val tokenRequest = HttpEntity(LinkedMultiValueMap(params), requestHeaders)

        val tokenResponse = restTemplate.postForEntity("/oauth2/token", tokenRequest, Map::class.java)

        Assertions.assertEquals(HttpStatus.OK, tokenResponse.statusCode)

        return tokenResponse.body!!["access_token"]!! as String
    }
}
