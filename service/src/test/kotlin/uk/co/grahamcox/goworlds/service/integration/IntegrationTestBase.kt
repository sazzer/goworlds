package uk.co.grahamcox.goworlds.service.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseCleaner
import uk.co.grahamcox.goworlds.service.integration.database.Seeder

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
        val update = jdbcTemplate.update(data.seedSql, data.seedParams)
        LOG.debug("Seeded {} rows with {}", update, data)
        return data
    }
}
