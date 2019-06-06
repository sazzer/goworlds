package uk.co.grahamcox.goworlds.service.users.dao

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * Interface describing how to create a user
 */
interface UserCreator {
    /**
     * Create a new user
     * @param data The data for the user
     * @return the created user
     */
    fun createUser(data: UserData) : Model<UserId, UserData>
}
