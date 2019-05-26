package uk.co.grahamcox.goworlds.service.users.http

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import uk.co.grahamcox.goworlds.service.http.buildUri
import uk.co.grahamcox.goworlds.service.users.UnknownUserException
import uk.co.grahamcox.goworlds.service.users.UserId
import uk.co.grahamcox.goworlds.service.users.UserRetriever
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.*

/**
 * Controller for interacting with Users
 */
@RestController
@RequestMapping("/users")
class UsersController(
        private val userRetriever: UserRetriever
) {
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return the user
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getUserById(@PathVariable("id") id: String) : UserModel {
        val userId = try {
            UserId(UUID.fromString(id))
        } catch (e: IllegalArgumentException) {
            throw UnknownUserException(null)
        }
        val user = userRetriever.getUserById(userId)

        return UserModel(
                self = UsersController::getUserById.buildUri(id),
                created = user.identity.created,
                updated = user.identity.updated,
                name = user.data.name,
                email = user.data.email
        )
    }

    /**
     * Handler for when the requested user can't be found
     */
    @ExceptionHandler(UnknownUserException::class)
    fun handleUnknownUser() = unknownUserProblem()
}
