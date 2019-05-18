package uk.co.grahamcox.goworlds.service.acceptance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase

/**
 * Check that the healthchecks return as expected
 */
class HealthIT : IntegrationTestBase() {
    /**
     * Test the healthchecks
     */
    @Test
    fun testHealth() {
        val response = restTemplate.getForEntity("/actuator/health", Map::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) }
        )

    }
}
