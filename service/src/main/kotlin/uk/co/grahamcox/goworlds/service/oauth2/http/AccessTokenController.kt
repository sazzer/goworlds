package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * Controller for introspection of access tokens
 */
@RestController
@RequestMapping("/oauth2/token")
class AccessTokenController {
    /**
     * Get the access token itself
     */
    @RequestMapping("/details")
    fun accessToken(accessToken: AccessToken?) = accessToken

    /**
     * Get the User ID
     */
    @RequestMapping("/userId")
    fun userId(userId: UserId?) = userId

    /**
     * Get the access token itself
     */
    @RequestMapping("/details/required")
    fun accessTokenRequired(accessToken: AccessToken) = accessToken

    /**
     * Get the User ID
     */
    @RequestMapping("/userId/required")
    fun userIdRequired(userId: UserId) = userId
}
