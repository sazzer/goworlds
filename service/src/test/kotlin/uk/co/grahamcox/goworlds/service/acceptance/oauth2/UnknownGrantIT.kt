package uk.co.grahamcox.goworlds.service.acceptance.oauth2

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus


/**
 * Tests for the OAuth2 Token Controller doing an unsupported auth
 */
class UnknownGrantIT : TokenTestBase() {
    /**
     * Test when no parameters are provided
     */
    @Test
    fun testNoParams() {
        val response = makeRequest(emptyMap())

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    /**
     * Test when the Grant Type is blank
     */
    @Test
    fun testBlankGrantType() {
        val params = mapOf(
                "grant_type" to listOf("")
        )

        val response = makeRequest(params)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("invalid_request", response.body!!["error"]) }
        )
    }

    /**
     * Test when the Grant Type is unknown
     */
    @Test
    fun testUnknownGrantType() {
        val params = mapOf(
                "grant_type" to listOf("unknown")
        )

        val response = makeRequest(params)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("unsupported_grant_type", response.body!!["error"]) }
        )
    }

    /**
     * Test when the Grant Type is unsupported
     */
    @Test
    fun testUnsupportedGrantType() {
        val params = mapOf(
                "grant_type" to listOf("password")
        )

        val response = makeRequest(params)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals("unsupported_grant_type", response.body!!["error"]) }
        )
    }

}
