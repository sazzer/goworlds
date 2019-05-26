package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Controller Advice for access token details
 */
@RestControllerAdvice
class AccessTokenControllerAdvice {
    /**
     * Handler for the Missing Access Token Exception
     */
    @ExceptionHandler(MissingAccessTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun missingAccessToken() {

    }
}
