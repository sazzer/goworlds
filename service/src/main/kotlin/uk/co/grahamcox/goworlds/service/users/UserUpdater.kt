package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Model
import java.util.*

/**
 * Interface describing how to update a user
 */
interface UserUpdater {
    /**
     * Update the user to the given data
     * @param userId The ID of the user
     * @param version The version of the user in the database
     * @param data The new data
     * @return the newly updated user
     */
    fun updateUser(userId: UserId, version: UUID, data: UserData) : Model<UserId, UserData>
}
