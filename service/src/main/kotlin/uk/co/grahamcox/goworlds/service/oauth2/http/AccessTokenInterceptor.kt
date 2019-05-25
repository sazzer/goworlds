package uk.co.grahamcox.goworlds.service.oauth2.http

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Interceptor for parsing the Access Token in a request
 */
class AccessTokenInterceptor(
        private val accessTokenSerializer: AccessTokenSerializer,
        private val accessTokenStore: AccessTokenStore
) : HandlerInterceptorAdapter() {
    companion object {
        /** The logger to use  */
        private val LOG = LoggerFactory.getLogger(AccessTokenInterceptor::class.java)
    }
    /**
     * Attempt to parse the Access Token from the request and store it for later use
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return try {
            val token = request.getHeader(HttpHeaders.AUTHORIZATION)
                    ?.let { header ->
                        LOG.debug("Processing header: {}", header)
                        header
                    }
                    ?.let { header ->
                        if (header.startsWith("Bearer ")) {
                            header.substring("Bearer ".length)
                        } else {
                            LOG.debug("Header is not a Bearer Token: {}", header)
                            null
                        }
                    }
                    ?.let { token ->
                            accessTokenSerializer.deserialize(token)
                    }
            accessTokenStore.accessToken = token
            true
        } catch (e: AccessTokenDeserializationException) {
            LOG.warn("Access Token {} is not valid", e.token, e)
            response.status = HttpStatus.UNAUTHORIZED.value()
            false
        }
    }

    /**
     * Clear the Access Token from the request
     */
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        accessTokenStore.accessToken = null
    }
}
