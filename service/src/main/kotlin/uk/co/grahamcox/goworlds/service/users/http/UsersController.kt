package uk.co.grahamcox.goworlds.service.users.http

import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.goworlds.service.http.buildUri
import uk.co.grahamcox.goworlds.service.http.collection.ResourceCollection
import uk.co.grahamcox.goworlds.service.http.sorts.parseSorts
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.users.*
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

        return buildUserModel(user)
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
        val parsedOffset = offset?.toLong() ?: 0
        val parsedCount = count?.toLong() ?: 10
        val parsedSorts = sort?.let { parseSorts<UserSort>(it) } ?: emptyList()

        // Actually fetch the users
        val users = userRetriever.searchUsers(
                filters = UserSearchFilters(name = name, email = email),
                offset = parsedOffset,
                count = parsedCount,
                sorts = parsedSorts
        )

        // Return the resources back to the client
        return ResourceCollection(
                offset = users.offset,
                total = users.total,
                self = UsersController::searchUsers.buildUri(name,
                        email,
                        sort,
                        users.offset.toString(),
                        parsedCount.toString()),
                next = if (parsedCount > 0 && users.hasNext) {
                    UsersController::searchUsers.buildUri(name,
                            email,
                            sort,
                            (users.offset + parsedCount).toString(),
                            parsedCount.toString())
                } else {
                    null
                },
                previous = if (parsedCount > 0 && users.hasPrevious) {
                    UsersController::searchUsers.buildUri(name,
                            email,
                            sort,
                            Math.max(0, users.offset - parsedCount).toString(),
                            parsedCount.toString())
                } else {
                    null
                },
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
                self = UsersController::getUserById.buildUri(user.identity.id.id.toString()),
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
