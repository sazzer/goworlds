package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Interceptor for parsing the Access Token in a request
 */
class AccessTokenInterceptor(
        private val accessTokenStore: AccessTokenStore
) : HandlerInterceptorAdapter() {
    /**
     * Attempt to parse the Access Token from the request and store it for later use
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return super.preHandle(request, response, handler)
    }

    /**
     * Clear the Access Token from the request
     */
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        accessTokenStore.accessToken = null
    }
}
