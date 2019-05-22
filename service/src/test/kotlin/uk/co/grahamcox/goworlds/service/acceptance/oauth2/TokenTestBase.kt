package uk.co.grahamcox.goworlds.service.acceptance.oauth2

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import uk.co.grahamcox.goworlds.service.integration.IntegrationTestBase

/**
 * Base class for tests of the OAuth2 Token endpoint
 */
class TokenTestBase : IntegrationTestBase() {

    /**
     * Make a request to the OAuth2 Token endpoint with the given body params
     * @param params The params to provide
     * @return the response
     */
    protected fun makeRequest(params: Map<String, List<String>>, headers: Map<String, String> = emptyMap()): ResponseEntity<Map<*, *>> {
        val requestHeaders = HttpHeaders()
        headers.forEach(requestHeaders::set)
        requestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body = LinkedMultiValueMap(params)

        val request = HttpEntity(body, requestHeaders)

        return restTemplate.postForEntity("/oauth2/token", request, Map::class.java)
    }
}
