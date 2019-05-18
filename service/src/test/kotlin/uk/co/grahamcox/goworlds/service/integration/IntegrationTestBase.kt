package uk.co.grahamcox.goworlds.service.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.goworlds.service.integration.database.DatabaseCleaner

/**
 * Base class for the Integration Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestConfig::class)
abstract class IntegrationTestBase {
    /** The RestTemplate to test with */
    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /**
     * Clean the database before each test
     */
    @BeforeEach
    fun cleanDatabase() {
        databaseCleaner.clean()
    }
}
