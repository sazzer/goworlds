package uk.co.grahamcox.goworlds.service.users.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.goworlds.service.http.buildUri
import uk.co.grahamcox.goworlds.service.http.collection.ResourceCollection
import uk.co.grahamcox.goworlds.service.http.parseSearchFields
import uk.co.grahamcox.goworlds.service.http.problem.ProblemModel
import uk.co.grahamcox.goworlds.service.http.problems.InvalidCountException
import uk.co.grahamcox.goworlds.service.http.problems.InvalidOffsetException
import uk.co.grahamcox.goworlds.service.http.problems.MissingRequestException
import uk.co.grahamcox.goworlds.service.http.problems.MissingRequestFieldException
import uk.co.grahamcox.goworlds.service.http.sorts.parseSorts
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.authorization.Authorizer
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.*
import java.net.URI
import java.util.*

/**
 * Controller for interacting with Users
 */
@RestController
@RequestMapping("/users")
class UsersController(
        private val userService: UserService
) {
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return the user
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getUserById(@PathVariable("id") id: String) : ResponseEntity<UserModel> {
        val userId = try {
            UserId(UUID.fromString(id))
        } catch (e: IllegalArgumentException) {
            throw UnknownUserException(null)
        }
        val user = userService.getById(userId)

        return buildUserResponse(user)
    }

    /**
     * Create a new user with the given details
     * @param input The input details from which to create a user
     * @return the created user
     */
    @RequestMapping(method = [RequestMethod.POST])
    fun createUser(@RequestBody input: UserInputModel?) : ResponseEntity<UserModel> {
        // Validate our inputs
        input ?: throw MissingRequestException()

        if (input.name.isNullOrBlank()) {
            throw MissingRequestFieldException("name")
        }
        if (input.email.isNullOrBlank()) {
            throw MissingRequestFieldException("email")
        }
        if (input.password.isNullOrBlank()) {
            throw MissingRequestFieldException("password")
        }

        // Actually create the user
        val user = userService.create(UserData(
                name = input.name,
                email = input.email,
                password = HashedPassword.hash(input.password)
        ))

        // And return the response
        return buildUserResponse(user)
    }

    /**
     * Update an existing user with the given details
     * @param id The ID of the user to update
     * @param input The input details from which to update the user
     * @return the created user
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PATCH])
    fun patchUser(@PathVariable("id") id: String,
                  @RequestBody input: UserInputModel?,
                  authorizer: Authorizer) : ResponseEntity<UserModel> {
        // Validate our inputs
        input ?: throw MissingRequestException()

        if (input.name?.isBlank() == true) {
            throw MissingRequestFieldException("name")
        }
        if (input.email?.isBlank() == true) {
            throw MissingRequestFieldException("email")
        }
        if (input.password?.isBlank() == true) {
            throw MissingRequestFieldException("password")
        }

        val userId = try {
            UserId(UUID.fromString(id))
        } catch (e: IllegalArgumentException) {
            throw UnknownUserException(null)
        }

        // Authorize the request
        authorizer {
            sameUser(userId)
        }

        // Actually update the user
        val user = userService.update(userId) { currentUser ->
            currentUser.data.copy(
                    name = input.name ?: currentUser.data.name,
                    email = input.email ?: currentUser.data.email,
                    password = input.password?.let(HashedPassword.Companion::hash) ?: currentUser.data.password
            )
        }

        // And return the response
        return buildUserResponse(user)
    }

    /**
     * Search users to get the set that match
     */
    @RequestMapping(method = [RequestMethod.GET])
    fun searchUsers(@RequestParam(name = "name", required = false) name: String?,
                    @RequestParam(name = "email", required = false) email: String?,
                    @RequestParam(name = "sort", required = false) sort: String?,
                    @RequestParam(name = "offset", required = false) offset: String?,
                    @RequestParam(name = "count", required = false) count: String?) : ResourceCollection<UserModel> {

        // Parse the inputs into values we can use, failing if any are invalid
        val parsedSearchFields = parseSearchFields<UserSort>(offset, count, sort)

        // Actually fetch the users
        val users = userService.search(
                filters = UserSearchFilters(name = name, email = email),
                offset = parsedSearchFields.offset,
                count = parsedSearchFields.count,
                sorts = parsedSearchFields.sorts
        )

        // Return the resources back to the client
        return ResourceCollection(
                offset = users.offset,
                total = users.total,
                entries = users.entries.map(this::buildUserModel)
        )
    }

    /**
     * Build the User Model response that represents the returned User
     * @param user The user to translate
     * @return the translated User Model
     */
    private fun buildUserModel(user: Model<UserId, UserData>): UserModel {
        return UserModel(
                id = user.identity.id.id.toString(),
                created = user.identity.created,
                updated = user.identity.updated,
                name = user.data.name,
                email = user.data.email
        )
    }

    /**
     * Build an HTTP Response for a single user
     * @param user The user
     * @return the response
     */
    private fun buildUserResponse(user: Model<UserId, UserData>) : ResponseEntity<UserModel> {
        val userModel = buildUserModel(user)
        return ResponseEntity.ok()
                .eTag("\"" + user.identity.version + "\"")
                .lastModified(user.identity.updated)
                .header(HttpHeaders.CONTENT_LOCATION, UsersController::getUserById.buildUri(user.identity.id.id.toString()).toString())
                .header("Accept-Patch", "application/merge-patch+json")
                .body(userModel)
    }

    /**
     * Handler for when the requested user can't be found
     */
    @ExceptionHandler(UnknownUserException::class)
    fun handleUnknownUser() = ProblemModel(
            type = URI("tag:goworlds,2019:users/problems/unknown-user"),
            title = "The requested user could not be found",
            statusCode = HttpStatus.NOT_FOUND
    )

    /**
     * Handler for when the provided email address is a duplicate
     */
    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail() = ProblemModel(
            type = URI("tag:goworlds,2019:users/problems/duplicate-email-address"),
            title = "The provided email address is already in use",
            statusCode = HttpStatus.CONFLICT
    )
}
