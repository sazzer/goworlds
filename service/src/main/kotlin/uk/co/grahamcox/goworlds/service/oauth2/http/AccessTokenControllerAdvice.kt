package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.goworlds.service.oauth2.authorization.AuthorizationFailedException

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

    /**
     * Handler for the Authorization Failed Exception
     */
    @ExceptionHandler(AuthorizationFailedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun authorizationFailed() {

    }
}
