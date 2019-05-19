package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for the Token endpoint for OAuth2
 */
@RestController
@RequestMapping("/oauth2/token")
class OAuth2TokenController {
    /**
     * Handle the POST method
     */
    @RequestMapping(method = [RequestMethod.POST])
    fun tokenHandler(): AccessTokenModel {
        return AccessTokenModel(
                accessToken = "accessToken",
                tokenType = "Bearer",
                expiry = 3600
        )
    }
}
