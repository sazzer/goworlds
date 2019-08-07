package uk.co.grahamcox.goworlds.service.users.http

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserRetriever

/**
 * Controller for determining if an Email Address is in use
 */
@RestController
@RequestMapping("/emails")
class EmailController(
        private val userRetriever: UserRetriever
) {
    /**
     * Check the given Email Address to see if it's already registered
     * @param email The email address
     * @return the result of the check
     */
    @RequestMapping(value = ["/{email}"], method = [RequestMethod.GET])
    fun getUserById(@PathVariable("email") email: String) : Map<String, Boolean> {
        val exists = try {
            userRetriever.getByEmail(email)
            true
        } catch (e: UnknownUserException) {
            false
        }

        return mapOf("exists" to exists)
    }
}
